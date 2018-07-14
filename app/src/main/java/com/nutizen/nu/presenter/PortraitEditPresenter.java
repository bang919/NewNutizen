package com.nutizen.nu.presenter;

import android.support.v4.app.Fragment;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.model.ViewerModel;
import com.nutizen.nu.view.PortraitEditView;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PortraitEditPresenter extends PhotoPresenter<PortraitEditView> {

    private ViewerModel mViewerModel;
    private File portraitFile;

    public PortraitEditPresenter(Fragment fragment, PortraitEditView view) {
        super(fragment, view);
        mViewerModel = new ViewerModel();
    }

    @Override
    public void onLuBanError(Throwable e) {
        mView.onError(e.getLocalizedMessage());
    }

    @Override
    public void onLuBanSuccess(File file) {
        portraitFile = file;
        mView.onLuBanSuccess(file);
    }

    public void deletePhotoFile() {
        if (portraitFile != null) {
            deletePhotoFile(portraitFile.getPath());
        }
    }

    public void savePortrait() {
        if (portraitFile == null) {
            mView.onError(mContext.getString(R.string.please_choose_your_new_portrait));
            return;
        }
        final LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        RequestBody requestBody = MultipartBody.create(MediaType.parse("application/x-jpg"), portraitFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("picture", System.currentTimeMillis() + accountMessage.getDetail().getViewer_nickname() + ".jpg", requestBody);

        Observable<LoginResponseBean> updatePortraitObser = mViewerModel.updatePortrait(part, accountMessage.getViewer_token())
                .flatMap(new Function<NormalResBean, ObservableSource<LoginResponseBean>>() {
                    @Override
                    public ObservableSource<LoginResponseBean> apply(NormalResBean normalResBean) throws Exception {
                        return mViewerModel.getViewerDetail(accountMessage)
                                .doOnNext(new Consumer<LoginResponseBean>() {
                                    @Override
                                    public void accept(LoginResponseBean loginResponseBean) throws Exception {
                                        LoginPresenter.updateLoginMessage(accountMessage);
                                    }
                                });
                    }
                });
        subscribeNetworkTask(getClass().getName().concat("savePortrait"), updatePortraitObser, new MyObserver<LoginResponseBean>() {
            @Override
            public void onMyNext(LoginResponseBean loginResponseBean) {
                mView.onUpdatePortraitSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
