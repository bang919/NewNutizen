package com.nutizen.nu.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.nutizen.nu.bean.request.ArchiveBody;
import com.nutizen.nu.bean.response.ArchiveResponse;
import com.nutizen.nu.bean.response.LiveStreamingResult;
import com.nutizen.nu.model.LiveStreamingModel;
import com.nutizen.nu.view.LiveStreamingView;

import java.io.File;

import io.reactivex.Observable;

public class LiveStreamingActivityPresenter extends PhotoPresenter<LiveStreamingView> {

    private LiveStreamingModel mLiveStreamingModel;
    private File portraitFile;

    public LiveStreamingActivityPresenter(Activity activity, LiveStreamingView view) {
        super(activity, view);
        mLiveStreamingModel = new LiveStreamingModel();
    }

    public void deletePhotoFile() {
        if (portraitFile != null) {
            deletePhotoFile(portraitFile.getPath());
            portraitFile = null;
        }
    }

    /**
     * @return true if message is enough to start live, false if message is not enough.
     */
    public boolean startLive(final String title, final boolean archive, final boolean audioOnly) {
        if (portraitFile == null) {
            mView.onError("Please select a live picture.");
            return false;
        }
        if (TextUtils.isEmpty(title)) {
            mView.onError("Please input a live title.");
            return false;
        }
        /*Observable<LiveStreamingResult> liveStreamingOb = mLiveStreamingModel.createLiveStreaming(title).flatMap(new Function<LiveStreamingResult, ObservableSource<LiveStreamingResult>>() {
            @Override
            public ObservableSource<LiveStreamingResult> apply(final LiveStreamingResult liveStreamingResult) throws Exception {
                return mLiveStreamingModel.updateLivePic(liveStreamingResult.getId(), portraitFile).map(new Function<NormalResBean, LiveStreamingResult>() {
                    @Override
                    public LiveStreamingResult apply(NormalResBean normalResBean) throws Exception {
                        deletePhotoFile();
                        return liveStreamingResult;
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        finishLive(liveStreamingResult);
                    }
                });
            }
        });*/
        Observable<LiveStreamingResult> liveStreamingOb = mLiveStreamingModel.createLiveStreaming(title);
        subscribeNetworkTask(getClass().getName().concat("startLive"), liveStreamingOb, new MyObserver<LiveStreamingResult>() {
            @Override
            public void onMyNext(LiveStreamingResult liveStreamingResult) {
                mView.onLiveStreamCreate(liveStreamingResult, title, archive, audioOnly);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
        return true;
    }

    public void startArchive(final ArchiveBody archiveBody) {
        if (archiveBody != null) {
            subscribeNetworkTask(getClass().getName().concat("startArchive"),
                    mLiveStreamingModel.startArchive(archiveBody).retry(3),
                    new MyObserver<ArchiveResponse>() {
                        @Override
                        public void onMyNext(ArchiveResponse archiveResponse) {
                            String[] split = archiveResponse.getPath().split("/");
                            archiveBody.setFilename(split[split.length - 1]);
                        }

                        @Override
                        public void onMyError(String errorMessage) {
                            mView.onError("Archive Failure: " + errorMessage);
                        }
                    });
        }
    }

    public void finishArchive(ArchiveBody archiveBody) {
        if (archiveBody != null && !TextUtils.isEmpty(archiveBody.getFilename())) {
            subscribeNetworkTask(mLiveStreamingModel.finishArchive(archiveBody));
        }
    }

    public void finishLive(LiveStreamingResult liveStreamingResult) {
        if (liveStreamingResult != null) {
            subscribeNetworkTask(mLiveStreamingModel.finishLive(liveStreamingResult.getId()).retry(3));
        }
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
}
