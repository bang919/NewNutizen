package com.nutizen.nu.fragment;

import android.view.View;
import android.view.WindowManager;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.presenter.PhotoPresenter;

public class LiveStreamingEditPicFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "LiveStreamingEditPicFragment";
    private LiveStreamingEditPicCallback mLiveStreamingEditPicCallback;

    public static LiveStreamingEditPicFragment getInstance() {
        return new LiveStreamingEditPicFragment();
    }

    public void setLiveStreamingEditPicCallback(LiveStreamingEditPicCallback liveStreamingEditPicCallback) {
        mLiveStreamingEditPicCallback = liveStreamingEditPicCallback;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_live_streaming_edit;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rootView.findViewById(R.id.root).setOnClickListener(this);
        rootView.findViewById(R.id.btn_take_photo).setOnClickListener(this);
        rootView.findViewById(R.id.btn_choose_library).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                if (mLiveStreamingEditPicCallback != null) {
                    mLiveStreamingEditPicCallback.chooseAFunctionToDo(PhotoPresenter.REQUEST_PERMISSION_CAMERA);
                }
                break;
            case R.id.btn_choose_library:
                if (mLiveStreamingEditPicCallback != null) {
                    mLiveStreamingEditPicCallback.chooseAFunctionToDo(PhotoPresenter.REQUEST_PERMISSION_ALBUM);
                }
                break;
        }
        dismiss();
    }

    public interface LiveStreamingEditPicCallback {
        void chooseAFunctionToDo(int requestPermissionCode);
    }
}
