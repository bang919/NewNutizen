package com.nutizen.nu.model;

import com.nutizen.nu.bean.request.ArchiveBody;
import com.nutizen.nu.bean.request.LiveStreamingBean;
import com.nutizen.nu.bean.response.ArchiveResponse;
import com.nutizen.nu.bean.response.LiveStreamingResult;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LiveStreamingModel {

    public Observable<LiveStreamingResult> createLiveStreaming(String title) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        LiveStreamingBean liveStreamingBean = new LiveStreamingBean(title, "Live",
                accountMessage.getDetail().getViewer_nickname() + ";" + accountMessage.getViewer_id(), "RTSP");
        return HttpClient.getApiInterface()
                .createLiveStreaming(liveStreamingBean, "bearer " + accountMessage.getViewer_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalResBean> updateLivePic(int liveId, File file) {
        RequestBody requestBody = MultipartBody.create(MediaType.parse("application/x-jpg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("thumbnail", System.currentTimeMillis() + file.getName() + ".jpg", requestBody);

        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        RequestBody live_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(liveId));
        RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), "live");
        return HttpClient.getApiInterface()
                .uploadLivePic(live_id, type, part, "bearer " + accountMessage.getViewer_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArchiveResponse> startArchive(ArchiveBody archiveBody) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        return HttpClient.getApiInterface()
                .startArchive("bearer " + accountMessage.getViewer_token(), archiveBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArchiveResponse> finishArchive(ArchiveBody archiveBody) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        return HttpClient.getApiInterface()
                .finishArchive("bearer " + accountMessage.getViewer_token(), archiveBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalResBean> finishLive(int liveId) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"id\":" + liveId + ",\"live_status\":0}");
        return HttpClient.getApiInterface()
                .changeLiveStatus(requestBody, "bearer " + accountMessage.getViewer_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
