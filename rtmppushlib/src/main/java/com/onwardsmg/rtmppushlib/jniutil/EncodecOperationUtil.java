package com.onwardsmg.rtmppushlib.jniutil;


/**
 * 转换和旋转二合一
 */
public class EncodecOperationUtil {

    static {
        System.loadLibrary("encodec_operation_util");
    }

    /**
     * NV21转换
     */
    public static native void nv21ToYUV420SemiPlanarRotate0(byte[] input, byte[] output, int width, int height);//NV21: YYYYYYYY VUVU YUV420sp: YYYYYYYY UVUV

    public static native void nv21ToYUV420SemiPlanarRotate90(byte[] input, byte[] output, int m_width, int m_height);

    /**
     * YV12转换
     */

    public static native void YV12toYUV420PlanarRotate0(byte[] input, byte[] output, int width, int height);

    public static native void YV12toYUV420PlanarRotate90(byte[] input, byte[] output, int width, int height);

    /**
     * 合并视频
     */
    public static native void mergeTwoYuv(byte[] input1, byte[] input2, byte[] output, int widthEach, int heightEach);
}
