package com.onwardsmg.rtmppushlib.bean;

public class RingBuffer {
    private static final int Length = 8;
    private int mSetPos = 0;
    private int mGetPos = 0;
    private byte[][] mArray;
    private Callback mCallback = null;

    public RingBuffer() {
        mArray = new byte[Length][];
    }

    public synchronized void setCallback(Callback callback) {
        mCallback = callback;
    }

    public synchronized void set(byte buffer[]) {
        int index = mSetPos % Length;
        byte[] old_buffer = mArray[index];
        mArray[index] = buffer;
        mSetPos++;
        if (old_buffer != null) {
            if (mCallback != null) {
                mCallback.onBufferRelease(old_buffer);
            }
            mGetPos++;
        }
    }

    public synchronized byte[] get() {
        if (mGetPos >= mSetPos) {
            return null;
        }
        int index = mGetPos % Length;
        byte[] buffer = mArray[index];
        mArray[index] = null;
        mGetPos++;
        return buffer;
    }

    public synchronized void release(byte[] buffer) {
        if (mCallback != null && buffer != null) {
            mCallback.onBufferRelease(buffer);
        }
    }

    public abstract static class Callback {
        public abstract void onBufferRelease(byte[] buffer);
    }
}
