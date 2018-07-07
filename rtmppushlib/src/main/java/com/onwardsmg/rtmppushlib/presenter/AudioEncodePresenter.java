package com.onwardsmg.rtmppushlib.presenter;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AudioEncodePresenter {

    public final static int SAMPLE_AUDIO_RATE_IN_HZ = 22050;
    private static final String AUDIO_MIME_TYPE = MediaFormat.MIMETYPE_AUDIO_AAC;

    private AudioRecord mAudioRecord;
    private MediaCodec mAudioEncoder;
    private AudioEncodeThread mAudioEncodeThread;
    private MediaCodec.BufferInfo mInfo;
    private AudioEncodeCallback mAudioEncodeCallback;
    private int mMinBufferSize;

    public void setAudioEncodeCallback(AudioEncodeCallback audioEncodeCallback) {
        mAudioEncodeCallback = audioEncodeCallback;
    }

    public void start() {
        mInfo = new MediaCodec.BufferInfo();
        mMinBufferSize = AudioRecord.getMinBufferSize(SAMPLE_AUDIO_RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        initAudioEncode(mMinBufferSize);
        mAudioEncoder.start();
        initAudioRecord(mMinBufferSize);
        if (mAudioEncodeThread != null) {
            mAudioEncodeThread.stopAudioEncoding();
            mAudioEncodeThread = null;
        }
        mAudioEncodeThread = new AudioEncodeThread();
        mAudioEncodeThread.start();
    }

    private void initAudioRecord(int minBufferSize) {
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, SAMPLE_AUDIO_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
        mAudioRecord.startRecording();
    }

    public void release() {
        try {
            if (mAudioRecord != null) {
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
            if (mAudioEncodeThread != null) {
                mAudioEncodeThread.stopAudioEncoding();
                mAudioEncodeThread = null;
            }
            stopEncoding();
            if (mAudioEncoder != null) {
                mAudioEncoder.stop();
                mAudioEncoder.release();
                mAudioEncoder = null;
            }
        } catch (IllegalStateException e) {
            if (mAudioEncoder != null) {
                mAudioEncoder.release();
                mAudioEncoder = null;
            }
            e.printStackTrace();
        }
    }

    private void stopEncoding() {
        //////////////////////// Stop Signal For Audio Encoder ///////////////////////////
        for (int i = 0; i < 3; i++) {
            if (mAudioEncoder == null) {
                break;
            }

            int inputBufferId = mAudioEncoder.dequeueInputBuffer(10000);

            if (inputBufferId >= 0) {
                mAudioEncoder.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initAudioEncode(int minBufferSize) {
        try {
            mAudioEncoder = MediaCodec.createEncoderByType(AUDIO_MIME_TYPE);
            mAudioEncoder.configure(getAudioFormat(minBufferSize), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaFormat getAudioFormat(int minBufferSize) {
        MediaFormat audioFormat = MediaFormat.createAudioFormat(AUDIO_MIME_TYPE, SAMPLE_AUDIO_RATE_IN_HZ, 1);
        audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, SAMPLE_AUDIO_RATE_IN_HZ);
        audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, minBufferSize);
        return audioFormat;
    }

    private class AudioEncodeThread extends Thread {


        private boolean audioEncodeStart;
        private ByteBuffer[] mInputBuffers;
        private ByteBuffer[] mOutputBuffers;
        private long initTime;
        private byte[] mAvcBuf;
        private byte[] recordBytes;
        private int roundTimes;
        private long roundOffset, previousPresentationTimeUs;

        public AudioEncodeThread() {
            super("Audio Codec Thread");
            initTime = System.currentTimeMillis();
            mAvcBuf = new byte[mMinBufferSize];
            recordBytes = new byte[mMinBufferSize];
            mInputBuffers = mAudioEncoder.getInputBuffers();
            mOutputBuffers = mAudioEncoder.getOutputBuffers();
            audioEncodeStart = true;
        }

        public void stopAudioEncoding() {
            audioEncodeStart = false;
        }

        private boolean input(byte[] data, int len, long timestamp) {
            try {
                if (!audioEncodeStart) {
                    return false;
                }
                for (int i = 0; i < 3; i++) {
                    int index = mAudioEncoder.dequeueInputBuffer(0);
                    if (index >= 0 && mAudioEncoder != null) {
                        ByteBuffer inputBuffer = mInputBuffers[index];
                        inputBuffer.clear();
                        int bufferRemaining = inputBuffer.remaining();
                        if (bufferRemaining < len) {
                            inputBuffer.put(data, 0, bufferRemaining);
                        } else {
                            inputBuffer.put(data, 0, len);
                        }
                        mAudioEncoder.queueInputBuffer(index, 0, len, timestamp, 0);
                        return true;
                    }
                }
                return false;
            } catch (IllegalStateException e) {
                release();
                return false;
            }
        }

        /**
         * 输出编码后的数据
         *
         * @param data 数据
         * @param len  有效数据长度
         * @return
         */
        private boolean output(byte[] data, int[] len, int[] time) {
            try {
                if (!audioEncodeStart) {
                    return false;
                }
                int i = mAudioEncoder.dequeueOutputBuffer(mInfo, 0);
                if (i >= 0 && mOutputBuffers != null) {
                    ByteBuffer outputBuffer;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        outputBuffer = mAudioEncoder.getOutputBuffer(i);
                    } else {
                        outputBuffer = mOutputBuffers[i];
                    }
                    if (mInfo.size > data.length) return false;
                    outputBuffer.position(mInfo.offset);
                    outputBuffer.limit(mInfo.offset + mInfo.size);

                    time[0] = getPresentationTime();

                    outputBuffer.get(data, 0, mInfo.size);
                    len[0] = mInfo.offset + mInfo.size;
                    mAudioEncoder.releaseOutputBuffer(i, false);
                } else if (i == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    mOutputBuffers = mAudioEncoder.getOutputBuffers();
                    return false;
                } else if (i == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//            mMF = mMediaCodec.getOutputFormat();
                    return false;
                } else if (i == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    return false;
                }
                return true;
            } catch (IllegalStateException e) {
                release();
                return false;
            }
        }

        private int getPresentationTime() {
            //there is a bug in audio encoder- it starts to give negative values after integer max size is exceeded
            //so getUnSignedInt need to be used
            if (previousPresentationTimeUs < 0 && mInfo.presentationTimeUs > 0) {
                roundTimes++;
                roundOffset = roundTimes * 4294967296L;
            }

            long presentationTimeInMillis = (roundOffset + getUnsignedInt(mInfo.presentationTimeUs)) / 1000;  //convert it to milliseconds
            //first it should be divided to 1000 and assign value to a long
            //then cast it to int -
            // Otherwise after about 35 minutes(exceeds integer max size) presentationTime will be negative
            //in this assignment int presentationTime = (int)info.presentationTimeUs/1000;  //convert it to milliseconds
            int presentationTime = (int) presentationTimeInMillis;
            previousPresentationTimeUs = mInfo.presentationTimeUs;
            return (int) presentationTimeInMillis;
        }

        private long getUnsignedInt(long x) {
            return x & 0xffffffffL;
        }

        private void sendDatas(int len, int timestamp) {
            byte[] mPacketBuf = new byte[len];
            if (mAudioEncodeCallback != null) {
                System.arraycopy(mAvcBuf, 0, mPacketBuf, 0, len);
                mAudioEncodeCallback.onAudioEncodePresenterOutput(mPacketBuf, timestamp);
            }
        }

        @Override
        public void run() {
            while (audioEncodeStart) {

                int read = mAudioRecord.read(recordBytes, 0, recordBytes.length);
                if (recordBytes != null && read > 0) {
                    input(recordBytes, recordBytes.length, (System.currentTimeMillis() - initTime) * 1000);

                    int[] len = new int[1];
                    int[] timestamp = new int[1];
                    while (output(mAvcBuf, len, timestamp)) {
                        sendDatas(len[0], timestamp[0]);//如果sps_pps已经初始化，就发送数据
                    }
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface AudioEncodeCallback {
        void onAudioEncodePresenterOutput(byte[] avcBuf, int timestamp);
    }
}
