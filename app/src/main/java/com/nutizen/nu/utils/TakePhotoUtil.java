package com.nutizen.nu.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TakePhotoUtil {
    /**
     * 把 content://media/external/images/media/5974 路径转化成 /storage/emulated/0/DCIM/Camera/IMG_20160807_.jpg 路径
     *
     * @param contentUri content://media/external/images/media/5974 路径
     * @return /storage/emulated/0/DCIM/Camera/IMG_20160807_.jpg 路径
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        if (!contentUri.toString().startsWith("content")) {
            return contentUri.getPath();
        }
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 获取图片文件的信息，是否旋转了，如果是则反转
     *
     * @param filePath /storage/emulated/0/DCIM/Camera/IMG_20160807_.jpg
     */
    public static void doRotateImageAndSaveStrategy(String filePath) {
        int rotate = readPictureDegree(filePath);
        if (rotate == 0)
            return;
        //得到sampleSize
        int[] out = new int[2];
        int sampleSize = caculateSampleSize(filePath, rotate, out);
        int outWidth = out[0];
        int outHeight = out[1];
        if (outWidth == 0 || outHeight == 0)
            return;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        //适当调整颜色深度
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            Bitmap srcBitmap = BitmapFactory.decodeStream(inputStream, null, options);//加载原图
            //test
            Bitmap.Config srcConfig = srcBitmap.getConfig();
            int srcMem = srcBitmap.getRowBytes() * srcBitmap.getHeight();//计算bitmap占用的内存大小

            Bitmap destBitmap = rotateImage(srcBitmap, rotate);
            int destMem = srcBitmap.getRowBytes() * srcBitmap.getHeight();
            srcBitmap.recycle();

            //保存bitmap到文件（覆盖原始图片）
            saveImageToSDCard(destBitmap, 100, filePath);
            destBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException ex) {
        }
        if (exifInterface == null)
            return degree;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }
        return degree;
    }

    //计算sampleSize
    private static int caculateSampleSize(String imgFilePath, int rotate, int[] out) {
        int imgWidth = 0;//原始图片的宽
        int imgHeight = 0;//原始图片的高
        int sampleSize = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imgFilePath);
            BitmapFactory.decodeStream(inputStream, null, options);//由于options.inJustDecodeBounds位true，所以这里并没有在内存中解码图片，只是为了得到原始图片的大小
            imgWidth = options.outWidth;
            imgHeight = options.outHeight;
            //初始化
            out[0] = imgWidth;
            out[1] = imgHeight;
            //如果旋转的角度是90的奇数倍,则输出的宽和高和原始宽高调换
            if ((rotate / 90) % 2 != 0) {
                out[0] = imgHeight;
                out[1] = imgWidth;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        //计算输出bitmap的sampleSize
        while (imgWidth / sampleSize > out[0] || imgHeight / sampleSize > out[1]) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    //通过img得到旋转rotate角度后的bitmap
    private static Bitmap rotateImage(Bitmap img, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, false);
        return img;
    }

    private static String saveImageToSDCard(Bitmap mbitmap, int quality, String filePath) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filePath);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, quality, outStream);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
