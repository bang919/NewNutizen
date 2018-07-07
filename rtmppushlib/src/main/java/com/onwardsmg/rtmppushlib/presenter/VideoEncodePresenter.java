package com.onwardsmg.rtmppushlib.presenter;

import android.graphics.ImageFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import com.onwardsmg.rtmppushlib.bean.RingBuffer;
import com.onwardsmg.rtmppushlib.jniutil.EncodecOperationUtil;
import com.onwardsmg.rtmppushlib.manager.MeetLibManagerImpl;
import com.onwardsmg.rtmppushlib.util.CameraUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoEncodePresenter {

    private boolean testMerge = false;

    private static final String MIME_TYPE = "video/avc";
    public static final int CODEC_FRAME_RATE = 15;
    private static final int FRAME_INTERVAL = 2;
    private int rotate;
    private int mFormat;
    private int codec_width;
    private int codec_height;
    private int m_SupportColorFormat;
    private RingBuffer mRingBuffer;

    private MediaCodecInfo mMediaCodecInfo;
    private MediaCodec mMediaCodec;
    private VideoEncodecThread mVideoEncodecThread;
    private VideoEncodeCallback mMediaEncodeCallback;

    public VideoEncodePresenter(int codec_width, int codec_height, int format, int orientation, RingBuffer ringBuffer) {
        this.codec_width = codec_width;
        this.codec_height = codec_height;
        this.mFormat = format;
        this.rotate = orientation;
        this.mRingBuffer = ringBuffer;
        mMediaCodecInfo = CameraUtil.getMediaCodecInfo();
        m_SupportColorFormat = CameraUtil.getSupportColorFormat(mMediaCodecInfo);
        //这里是验算C++算法的，不要删
//        byte[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
//                26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48};
//        byte[] b = new byte[48];
//        EncodecOperationUtil.YUV420spRotate90(a, b, 8, 4);
    }

    public void start() {
        initMediaEncode();
        mMediaCodec.start();
        if (mVideoEncodecThread != null) {
            mVideoEncodecThread.stopVidoEncoding();
            mVideoEncodecThread = null;
        }
        mVideoEncodecThread = new VideoEncodecThread();
        mVideoEncodecThread.start();
    }

    public void setVideoEncodeCallback(VideoEncodeCallback callback) {
        mMediaEncodeCallback = callback;
    }

    public void release() {
        if (mVideoEncodecThread != null) {
            mVideoEncodecThread.stopVidoEncoding();
            mVideoEncodecThread = null;
        }
        stopEncoding();
        if (mMediaCodec != null) {
            mMediaCodec.flush();
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
        }
    }

    private void stopEncoding() {
        //////////////////////// Stop Signal For Audio Encoder ///////////////////////////
        for (int i = 0; i < 3; i++) {
            if (mMediaCodec == null) {
                break;
            }

            int inputBufferId = mMediaCodec.dequeueInputBuffer(10000);

            if (inputBufferId >= 0) {
                mMediaCodec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initMediaEncode() {
        try {
            mMediaCodec = MediaCodec.createByCodecName(mMediaCodecInfo.getName());
            mMediaCodec.configure(getMediaCodecFormat(), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaFormat getMediaCodecFormat() {
//        final MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, codec_width, codec_height);//need Rotate90
        int tempWidth = rotate % 180 == 0 ? codec_width : codec_height;
        int tempHeight = rotate % 180 == 0 ? codec_height : codec_width;
        final MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, testMerge ? tempWidth * 2 : tempWidth, tempHeight);
        format.setInteger(MediaFormat.KEY_BIT_RATE, MeetLibManagerImpl.CODEC_BIT_RATE);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, CODEC_FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, FRAME_INTERVAL);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, m_SupportColorFormat);
        format.setInteger("stride", testMerge ? tempWidth * 2 : tempWidth);
        format.setInteger("slice-height", tempHeight);
        return format;
    }

    private class VideoEncodecThread extends Thread {

        private byte[] mRawData;
        private byte[] mAvcBuf;
        private ByteBuffer[] inputBuffers;
        private ByteBuffer[] outputBuffers;
        private byte[] sps_pps;
        private MediaCodec.BufferInfo mInfo;
        private boolean hadQuite;
        private long initTime;
        private int roundTimes;
        private long roundOffset, previousPresentationTimeUs;
        private int numInputFrames = 0;

        public VideoEncodecThread() {
            super("Video Codec Thread");
            initTime = System.currentTimeMillis();
            mInfo = new MediaCodec.BufferInfo();
            mAvcBuf = new byte[CameraUtil.getPreviewBufferSize(codec_width, codec_height, mFormat)];
            inputBuffers = mMediaCodec.getInputBuffers();
            outputBuffers = mMediaCodec.getOutputBuffers();
            hadQuite = false;
        }


        public void stopVidoEncoding() {
            hadQuite = true;
            outputBuffers = null;
            inputBuffers = null;
        }

        /**
         * 向编码器输入数据，此处要求输入YUV420P的数据
         *
         * @param data      YUV数据
         * @param len       数据长度
         * @param timestamp 时间戳
         */
        private boolean input(byte[] data, int len, long timestamp) {
            if (hadQuite) {
                return false;
            }
            int index = mMediaCodec.dequeueInputBuffer(0);
            if (index >= 0 && inputBuffers != null) {
                ByteBuffer inputBuffer = inputBuffers[index];
                inputBuffer.clear();
                if (inputBuffer.capacity() < len) {
                    mMediaCodec.queueInputBuffer(index, 0, 0, timestamp, 0);
                    return false;
                }
                inputBuffer.put(data, 0, len);
                mMediaCodec.queueInputBuffer(index, 0, len, timestamp, 0);
            } else {
                return false;
            }
            return true;
        }

        /**
         * 输出编码后的数据
         *
         * @param data 数据
         * @param len  有效数据长度
         * @param ts   时间戳
         * @return
         */
        private boolean output(byte[] data, int[] len, int[] ts) {
            if (hadQuite) {
                return false;
            }
            int i = mMediaCodec.dequeueOutputBuffer(mInfo, 0);
            if (i >= 0 && outputBuffers != null) {
                if (mInfo.size > data.length) return false;
                outputBuffers[i].position(mInfo.offset);
                outputBuffers[i].limit(mInfo.offset + mInfo.size);

                ts[0] = getPresentationTime();

                outputBuffers[i].get(data, 0, mInfo.size);
                len[0] = mInfo.offset + mInfo.size;
                mMediaCodec.releaseOutputBuffer(i, false);
            } else if (i == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                outputBuffers = mMediaCodec.getOutputBuffers();
                return false;
            } else if (i == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//            mMF = mMediaCodec.getOutputFormat();
                return false;
            } else if (i == MediaCodec.INFO_TRY_AGAIN_LATER) {
                return false;
            }

            return true;
        }


        @Override
        public void run() {
            if (hadQuite) {
                return;
            }
            while (!hadQuite) {
                byte[] data = mRingBuffer.get();
                if (data != null) {
                    if (mRawData == null || mRawData.length < data.length) {
                        mRawData = new byte[data.length];
                    }
                    if (mFormat == ImageFormat.YV12) {
                        swapYV12(data, mRawData, codec_width, codec_height);
                    } else if (mFormat == ImageFormat.NV21) {
                        swapNV21(data, mRawData, codec_width, codec_height);
                    } else {
                        System.arraycopy(data, 0, mRawData, 0, data.length);
                    }

                    if (testMerge) {
                        //copy
                        byte[] out = new byte[mRawData.length * 2];
                        int tempWidth = rotate % 180 == 0 ? codec_width : codec_height;
                        int tempHeight = rotate % 180 == 0 ? codec_height : codec_width;
                        EncodecOperationUtil.mergeTwoYuv(mRawData, mRawData, out, tempWidth, tempHeight);
                        input(out, data.length * 2, (System.currentTimeMillis() - initTime) * 1000);
                    } else {
                        input(mRawData, data.length, (System.currentTimeMillis() - initTime) * 1000);
                    }

                    numInputFrames++;
                    mRingBuffer.release(data);

                    int[] len = new int[1];
                    int[] ts = new int[1];
                    while (output(mAvcBuf, len, ts)) {
                        sendDatas(len[0], ts[0]);//如果sps_pps已经初始化，就发送数据
                        if (sps_pps == null) {//初始化sps_pps
                            sps_pps = new byte[len[0]];
                            System.arraycopy(mAvcBuf, 0, sps_pps, 0, len[0]);
                        }
                    }
                }

                try {
                    Thread.sleep(1000 / CODEC_FRAME_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

        private void sendDatas(int len, int timeUs) {
            if (mMediaEncodeCallback != null && sps_pps != null) {//如果sps_pps已经初始化，就发送数据
                byte[] mPacketBuf;
                if (mAvcBuf[4] == 0x65) {
                    mPacketBuf = new byte[len + sps_pps.length];
                    System.arraycopy(sps_pps, 0, mPacketBuf, 0, sps_pps.length);
                    System.arraycopy(mAvcBuf, 0, mPacketBuf, sps_pps.length, len);
//                    len += sps_pps.length;
                } else {
                    mPacketBuf = new byte[len];
                    System.arraycopy(mAvcBuf, 0, mPacketBuf, 0, len);
                }
                mMediaEncodeCallback.onVideoEncodePresenterOutput(mPacketBuf, timeUs);
            }
        }

        private void swapYV12(byte[] input, byte[] output, int m_width, int m_height) {
            if (m_SupportColorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar) {
                switch (rotate) {
                    case 0:
                        EncodecOperationUtil.YV12toYUV420PlanarRotate0(input, output, m_width, m_height);
                        break;
                    case 90:
                        EncodecOperationUtil.YV12toYUV420PlanarRotate90(input, output, m_width, m_height);
                        break;
                    default:
                        EncodecOperationUtil.YV12toYUV420PlanarRotate0(input, output, m_width, m_height);
                        break;
                }
            } else {
                System.arraycopy(input, 0, output, 0, input.length);
            }
        }

        private void swapNV21(byte[] input, byte[] output, int m_width, int m_height) {
            if (m_SupportColorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar) {
                switch (rotate) {
                    case 0:
                        EncodecOperationUtil.nv21ToYUV420SemiPlanarRotate0(input, output, m_width, m_height);
                        break;
                    case 90:
                        EncodecOperationUtil.nv21ToYUV420SemiPlanarRotate90(input, output, m_width, m_height);
                        break;
                    default:
                        EncodecOperationUtil.nv21ToYUV420SemiPlanarRotate0(input, output, m_width, m_height);
                        break;
                }
            } else {
                System.arraycopy(input, 0, output, 0, input.length);
            }
        }
    }

    public interface VideoEncodeCallback {
        void onVideoEncodePresenterOutput(byte[] avcBuf, int timeUs);
    }
}
