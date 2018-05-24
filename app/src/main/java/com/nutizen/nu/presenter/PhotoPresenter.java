package com.nutizen.nu.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.TakePhotoUtil;
import com.nutizen.nu.utils.ToastUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

public abstract class PhotoPresenter<V> extends BasePresenter<V> {

    private Fragment mFragment;

    private final int REQUEST_PERMISSION = 0x9;
    private final int REQUEST_CAMERA = 0x10;
    private final int REQUEST_ALBUM = 0x11;

    private Uri mPhotoUri;


    public PhotoPresenter(Fragment fragment, V view) {
        super(fragment.getContext(), view);
        mFragment = fragment;
    }


    /**
     * 启动照相机照相
     */
    public void selectTakePhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mFragment.getContext(), Manifest.permission.CAMERA);
            int checkCallPhonePermission2 = ContextCompat.checkSelfPermission(mFragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED || checkCallPhonePermission2 != PackageManager.PERMISSION_GRANTED) {
                mFragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return;
            } else {
                takePhoto();
            }
        } else {
            takePhoto();
        }
    }

    /**
     * 调用相册
     */
    public void selectAlbum() {
        Intent getAlbum = new Intent(Intent.ACTION_PICK, null);
        getAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mFragment.startActivityForResult(getAlbum, REQUEST_ALBUM);
    }

    private void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory() +
                    File.separator + Environment.DIRECTORY_DCIM + File.separator;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            String fileName = getPhotoFileName().concat(".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //mPhotoUri = FileProvider.getUriForFile(mContext, mFragment.getActivity().getPackageName() + ".fileprovider", new File(path + fileName));

            //  7.0之后直接用file://会出错，需要用content://
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path + fileName);
            mPhotoUri = mFragment.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);//TODO 这里CROP自动旋转了
            mFragment.startActivityForResult(intent, REQUEST_CAMERA);
        } else {
            ToastUtils.showShort("No SD card.");
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "IMG_" + dateFormat.format(date);
    }

    /**
     * 在fragment的onActivityResult调用
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (mPhotoUri != null) {//如果是camera take photo的，但是出错了哦，删去照相的图片
                deletePhotoFile(mPhotoUri);
                mPhotoUri = null;
            }
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                Uri uriCamera = null;
                if (data != null && data.getData() != null) {
                    uriCamera = data.getData();
                }
                if (uriCamera == null) {
                    uriCamera = mPhotoUri;
                }
                startPhotoZoom(uriCamera);
                break;
            case REQUEST_ALBUM:
                Uri uriAlbum = data.getData();
                startPhotoZoom(uriAlbum);
                break;
            case UCrop.REQUEST_CROP://UCrop库
                if (mPhotoUri != null) {//如果是camera take photo的，删去照相的图片
                    deletePhotoFile(mPhotoUri);
                    mPhotoUri = null;
                }
                Uri uriCrop = UCrop.getOutput(data);
                jumpToLuban(uriCrop, true);
                break;
        }
    }

    public void startPhotoZoom(Uri uri) {
        //看看要不要旋转
        TakePhotoUtil.doRotateImageAndSaveStrategy(TakePhotoUtil.getRealPathFromUri(mContext, uri));

        //用UCrop裁剪图片
        String realPathFromUri = TakePhotoUtil.getRealPathFromUri(mContext, uri);
        UCrop.of(Uri.fromFile(new File(realPathFromUri)), Uri.fromFile(new File(realPathFromUri.substring(0, realPathFromUri.lastIndexOf("/")).concat("nutemp.jpg"))))
                .withAspectRatio(1, 1)
                .start(mContext, mFragment, UCrop.REQUEST_CROP);
    }

    //用Luban压缩图片
    private void jumpToLuban(final Uri photoUri, final boolean deleteSource) {
        String path = TakePhotoUtil.getRealPathFromUri(mFragment.getContext(), photoUri);
        Observable.just(path)
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String path) throws Exception {
                        return Luban.with(mContext).load(new File(path)).get();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(File file) {
                        onLuBanSuccess(file);//传出去Luban cache图片
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLuBanError(e);
                        if (deleteSource) {//删去裁剪的图片
                            deletePhotoFile(photoUri);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (deleteSource) {//删去裁剪的图片
                            deletePhotoFile(photoUri);
                        }
                    }
                });
    }


    /**
     * 删除保存头像的文件夹
     */
    public void deletePhotoFile(Uri photoFileName) {
        String path = photoFileName.toString();
        if (path.startsWith("content")) {
            path = TakePhotoUtil.getRealPathFromUri(mFragment.getContext(), photoFileName);
        }
        deletePhotoFile(path);
    }

    public void deletePhotoFile(String filePath) {
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        if (file.exists()) {
            file.delete();
        }

        //发送广播更新相册
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        mFragment.getContext().sendBroadcast(intent);
    }

    public abstract void onLuBanError(Throwable e);

    public abstract void onLuBanSuccess(File file);

    /**
     * 在fragment的onRequestPermissionsResult调用
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(mFragment.getContext(), mFragment.getString(R.string.no_permission_for_camera), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
