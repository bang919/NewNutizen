package com.nutizen.nu.activity;

import android.app.Service;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.BasePlayerAdapter;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.fragment.ShareFragment;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.presenter.PlayerActivityPresenter;
import com.nutizen.nu.receiver.HeadsetReceiver;
import com.nutizen.nu.utils.DialogUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.BasePlayerActivityView;

import java.util.ArrayList;

public abstract class PlayerActivity<D, P extends PlayerActivityPresenter> extends BaseActivity<P> implements BasePlayerActivityView, View.OnClickListener, BasePlayerAdapter.CommentAdapterCallback, ShareFragment.OnSharePlatformClickListener {

    public static final String DATA_BEAN = "data bean";

    protected D mData;
    private View mFavouriteBtn;
    private View mProgressBar;
    private View mTopHeadView;
    protected RecyclerView mMessageAndCommentRv;
    protected SimpleExoPlayerView mSimpleExoPlayerView;
    protected BasePlayerAdapter mBasePlayerAdapter;

    private boolean initFavourite;//一开始是喜爱还是不喜爱，用于editFavourite
    private Handler mHandler;
    private Runnable favouriteRunnable;

    private ShareFragment mShareFragment;
    private HeadsetReceiver mHeadsetReceiver;//耳机的receiver

    @Override
    public int getBarColor() {
        return Constants.NULL_COLOR;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_content_player;
    }


    @Override
    protected void initView() {
        mSimpleExoPlayerView = findViewById(R.id.simple_player_contentplayer);
        mMessageAndCommentRv = findViewById(R.id.rv_message_and_comment);
        mProgressBar = findViewById(R.id.progress_bar_layout);
        mTopHeadView = findViewById(R.id.top_view);

        mPresenter.setSimpleExoPlayerView(mSimpleExoPlayerView);
    }

    @Override
    protected void initData() {
        mHandler = new Handler();
        //setDataBean在live直接能获取到url，就能初始化prepare SimpleExoPlayerView，前提是SimpleExoPlayerView已经ready
        mSimpleExoPlayerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initHeadDatas();

                //live初始化adapter才能找到favouriteBtn啊
                initFavouriteBtn();

                mSimpleExoPlayerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        favouriteRunnable = new Runnable() {
            @Override
            public void run() {
                LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
                boolean isSelected = mFavouriteBtn != null && mFavouriteBtn.isSelected();
                if (accountMessage != null && initFavourite != isSelected) {
                    initFavourite = isSelected;
                    editFavourite(isSelected);
                }
            }
        };
    }

    private void initHeadDatas() {
        mData = setDataBean();
        mPresenter.getDatas(mData);

        //adapter用到mData，所以放在mData后
        mBasePlayerAdapter = createBasePlayerAdapter();
        mMessageAndCommentRv.setAdapter(mBasePlayerAdapter);
        mBasePlayerAdapter.setListener(PlayerActivity.this);
    }

    private void initFavouriteBtn() {
        mFavouriteBtn = setFavouriteBtn();
        if (mFavouriteBtn != null) {
            mFavouriteBtn.setVisibility(View.VISIBLE);
            mFavouriteBtn.setOnClickListener(PlayerActivity.this);
            mFavouriteBtn.setSelected(initFavourite);
        }
    }

    @Override
    protected void initEvent() {

        mMessageAndCommentRv.setLayoutManager(new LinearLayoutManager(this));

        mSimpleExoPlayerView.findViewById(R.id.iv_back).setOnClickListener(this);

        mSimpleExoPlayerView.findViewById(R.id.iv_share).setOnClickListener(this);

        mSimpleExoPlayerView.findViewById(R.id.exo_fullscreen).setOnClickListener(this);

        registerHeadsetReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpeakerPhoneOn(true);
        if (mPresenter != null) {
            mPresenter.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        setSpeakerPhoneOn(false);
        if (mPresenter != null) {
            mPresenter.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mHeadsetReceiver != null) {
            unregisterReceiver(mHeadsetReceiver);
        }
        mPresenter.releasePlayer();
        checkFavouriteRequest(true);
        super.onDestroy();
    }


    @Override
    public void onCommentListResponse(ArrayList<CommentResult> comments) {
        mBasePlayerAdapter.initAvailableWidth(mMessageAndCommentRv.getWidth());
        mBasePlayerAdapter.setCommentData(comments);
    }

    @Override
    public void onSuccess() {
        setLoadingVisibility(false);
        mPresenter.setPlayWhenReady(true);
    }

    @Override
    public void onFailure(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mPresenter.isFullScreen()) {
                    mPresenter.switchPlayerSize(this, mTopHeadView, !mPresenter.isFullScreen());
                } else {
                    finish();
                }
                break;
            case R.id.iv_share:
                if (mShareFragment == null) {
                    mShareFragment = ShareFragment.getInstance();
                    mShareFragment.setOnSharePlatformClickListener(this);
                }
                mPresenter.setPlayWhenReady(false);
                mShareFragment.show(getSupportFragmentManager(), ShareFragment.TAG);
                break;
            case R.id.iv_favourite:
                if (checkLogin()) {
                    v.setSelected(!v.isSelected());
                    checkFavouriteRequest(false);
                }
                break;
            case R.id.exo_fullscreen:
                mPresenter.switchPlayerSize(this, mTopHeadView, !mPresenter.isFullScreen());
                break;
        }
    }

    protected boolean checkLogin() {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage == null) {
            DialogUtils.getAskLoginDialog(this).show();
            return false;
        }
        return true;
    }

    protected void setIsFavourite(boolean isFavourite) {
        initFavourite = isFavourite;
        if (mFavouriteBtn != null)
            mFavouriteBtn.setSelected(initFavourite);
    }

    protected void setLoadingVisibility(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void checkFavouriteRequest(boolean withoutDelay) {//看看是否需要请求更改喜爱状态
        mHandler.removeCallbacks(favouriteRunnable);
        mHandler.postDelayed(favouriteRunnable, withoutDelay ? 0 : 3000);//3秒内连续按无效
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPresenter.isFullScreen() && (event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE || event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            mPresenter.switchPlayerSize(this, mTopHeadView, !mPresenter.isFullScreen());
            return true;
        }
        //媒体音乐音量
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                    am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
                    break;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                    am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void commitComment(String commit) {
        if (checkLogin()) {
            mPresenter.commitComment(returnCommentType(), returnCommentId(), commit);
            setLoadingVisibility(true);
        }
    }

    @Override
    public void showMore() {

    }

    @Override
    public void longClickToDelete(CommentResult comment) {
        mPresenter.deleteComment(returnCommentType(), returnCommentId(), comment);
        setLoadingVisibility(true);
    }

    @Override
    public void onSharePlatformClick(String platformName) {
        setLoadingVisibility(true);
        mPresenter.shareToPlatform(mData, platformName);
    }

    protected abstract D setDataBean();

    protected D getDataBean() {
        return mData;
    }

    protected abstract BasePlayerAdapter createBasePlayerAdapter();

    protected abstract String returnCommentType();

    protected abstract int returnCommentId();

    protected abstract View setFavouriteBtn();

    protected abstract void editFavourite(boolean isfavourite);

    private void registerHeadsetReceiver() {
        mHeadsetReceiver = new HeadsetReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(mHeadsetReceiver, intentFilter);

        // for bluetooth headset connection receiver
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mHeadsetReceiver, bluetoothFilter);
    }

    private void setSpeakerPhoneOn(boolean on) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (on) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);

            //5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                //设置音量，解决有些机型切换后没声音或者声音突然变大的问题
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FX_KEY_CLICK);

            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FX_KEY_CLICK);
            }
        }

    }

}
