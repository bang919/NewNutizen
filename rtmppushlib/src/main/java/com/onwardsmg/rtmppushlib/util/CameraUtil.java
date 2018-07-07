package com.onwardsmg.rtmppushlib.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.List;

public class CameraUtil {

    // 获取当前窗口管理器显示方向
    public static int getDisplayOrientation(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        android.hardware.Camera.CameraInfo camInfo =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, camInfo);

        // 这里其实还是不太懂：为什么要获取camInfo的方向呢？相当于相机标定？？
        int result = (camInfo.orientation - degrees + 360) % 360;

        return result;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static int getSupportedPreviewFormats(Camera camera) {
        int supportColorFormat = getSupportColorFormat(getMediaCodecInfo());
        int format = supportColorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar ? ImageFormat.NV21 : ImageFormat.YV12;
        boolean supportCanFind = false;
        List<Integer> previewFormats = camera.getParameters().getSupportedPreviewFormats();
        for (Integer i : previewFormats) {
            if (i == format) {
                supportCanFind = true;
            }
        }
        if (!supportCanFind) {
            format = previewFormats.get(0);
        }
        return format;
    }

    public static int getPreviewBufferSize(int width, int height, int format) {
        int size = 0;
        switch (format) {
            case ImageFormat.YV12: {
                int yStride = (int) Math.ceil(width / 16.0) * 16;
                int uvStride = (int) Math.ceil((yStride / 2) / 16.0) * 16;
                int ySize = yStride * height;
                int uvSize = uvStride * height / 2;
                size = ySize + uvSize * 2;
            }
            break;

            case ImageFormat.NV21: {
                float bytesPerPix = (float) ImageFormat.getBitsPerPixel(format) / 8;
                size = (int) (width * height * bytesPerPix);
            }
            break;
        }

        return size;
    }


    public static MediaCodecInfo getMediaCodecInfo() {
        int numCodecs = MediaCodecList.getCodecCount();
        MediaCodecInfo codecInfo = null;
        for (int i = 0; i < numCodecs && codecInfo == null; i++) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            String[] types = info.getSupportedTypes();
            boolean found = false;
            for (int j = 0; j < types.length && !found; j++) {
                if (types[j].equals("video/avc")) {
                    System.out.println("found");
                    found = true;
                }
            }
            if (!found)
                continue;
            codecInfo = info;
        }
        return codecInfo;
    }

    public static int getSupportColorFormat(MediaCodecInfo codecInfo) {


        Log.e("AvcEncoder", "Found " + codecInfo.getName() + " supporting " + "video/avc");

        // Find a color profile that the codec supports
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType("video/avc");
        Log.e("AvcEncoder",
                "length-" + capabilities.colorFormats.length + "==" + Arrays.toString(capabilities.colorFormats));

        for (int i = 0; i < capabilities.colorFormats.length; i++) {

            switch (capabilities.colorFormats[i]) {
                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
                case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:

                    Log.e("AvcEncoder", "supported color format::" + capabilities.colorFormats[i]);
                    return capabilities.colorFormats[i];
                default:
                    Log.e("AvcEncoder", "unsupported color format " + capabilities.colorFormats[i]);
                    break;
            }
        }

        return -1;
    }
}
