package com.onwardsmg.rtmppushlib.presenter;

import net.butterflytv.rtmp_client.RTMPMuxer;

public class RtmpAssemblePresenter {

    private String rtmpUrl;
    private RTMPMuxer mRTMPMuxer;
    private int preTime;
    private byte[] lock;

    public RtmpAssemblePresenter(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
        mRTMPMuxer = new RTMPMuxer();
        lock = new byte[0];
        checkAndConnect();
    }

    public void release() {
        mRTMPMuxer.close();
    }

    public void setRtmpVideoDatas(byte[] bytes1, int timestamp) {
        synchronized (lock) {
            if (preTime >= timestamp) {
                preTime = preTime + 1;
            } else {
                preTime = timestamp;
            }
            int i = mRTMPMuxer.writeVideo(bytes1, 0, bytes1.length, preTime);
        }
    }

    public void setRtmpAudioDatas(byte[] bytes1, int timestamp) {
        synchronized (lock) {
            if (preTime >= timestamp) {
                preTime = preTime + 1;
            } else {
                preTime = timestamp;
            }
            int i = mRTMPMuxer.writeAudio(bytes1, 0, bytes1.length, preTime);
        }
    }

    private void checkAndConnect() {
        if (mRTMPMuxer.isConnected() == 0) {
            mRTMPMuxer.open(rtmpUrl, 0, 0);
        }
    }
}
