package com.nutizen.nu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.ProfileSettingAdapter;
import com.nutizen.nu.adapter.VodRecyclerViewAdapter;
import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.request.WatchHistoryCountBody;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.presenter.ContentPlayerPresenter;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.DialogUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.ContentPlayerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContentPlayerActivity extends BaseActivity<ContentPlayerPresenter> implements ContentPlayerView, View.OnClickListener, VodRecyclerViewAdapter.CommentAdapterCallback, ProfileSettingAdapter.OnProfileSelectListener {

    public static final String CONTENT_BEAN = "content bean";
    private View mFavouriteBtn;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private RecyclerView mMessageAndCommentRv;
    private RecyclerView mProfileSettingRv;
    private VodRecyclerViewAdapter mVodRecyclerViewAdapter;
    private ProfileSettingAdapter mProfileSettingAdapter;

    private ContentResponseBean.SearchBean mContentBean;
    private ContentPlaybackBean mContentPlaybackBean;
    private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String mStartTime;//用于addWatchCount
    private boolean initFavourite;//一开始是喜爱还是不喜爱，用于editFavourite

    @Override
    public int getBarColor() {
        return Constants.NULL_COLOR;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_content_player;
    }

    @Override
    protected ContentPlayerPresenter initPresenter() {
        return new ContentPlayerPresenter(this, this);
    }

    @Override
    protected void initView() {
        mSimpleExoPlayerView = findViewById(R.id.simple_player_contentplayer);
        mMessageAndCommentRv = findViewById(R.id.rv_message_and_comment);
        mProfileSettingRv = findViewById(R.id.rv_profile_settings);
        mFavouriteBtn = findViewById(R.id.iv_favourite);
    }

    @Override
    protected void initData() {
        mPresenter.setSimpleExoPlayerView(mSimpleExoPlayerView);

        Intent intent = getIntent();
        mContentBean = (ContentResponseBean.SearchBean) intent.getSerializableExtra(CONTENT_BEAN);
        mPresenter.getDatas(mContentBean);

        mVodRecyclerViewAdapter = new VodRecyclerViewAdapter(this, mContentBean);
        mMessageAndCommentRv.setAdapter(mVodRecyclerViewAdapter);
        mMessageAndCommentRv.setLayoutManager(new LinearLayoutManager(this));
        mProfileSettingAdapter = new ProfileSettingAdapter();
        mProfileSettingAdapter.setOnProfileSelectListener(this);
        mProfileSettingRv.setAdapter(mProfileSettingAdapter);
        mProfileSettingRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        mSimpleExoPlayerView.findViewById(R.id.iv_back).setOnClickListener(this);
        mSimpleExoPlayerView.findViewById(R.id.iv_download).setOnClickListener(this);
        mSimpleExoPlayerView.findViewById(R.id.iv_share).setOnClickListener(this);
        mSimpleExoPlayerView.findViewById(R.id.iv_settings).setOnClickListener(this);
        mSimpleExoPlayerView.findViewById(R.id.exo_fullscreen).setOnClickListener(this);
        mFavouriteBtn.setOnClickListener(this);
        mVodRecyclerViewAdapter.setListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.preparePlayer();
        }
    }

    @Override
    protected void onPause() {
        if (mPresenter != null) {
            mPresenter.releasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        String endTime = sDateFormat.format(new Date());
        if (mContentPlaybackBean != null && mStartTime != null) {
            WatchHistoryCountBody watchHistoryCountBody = new WatchHistoryCountBody();
            watchHistoryCountBody.setContent_name(mContentBean.getTitle());
            watchHistoryCountBody.setContent_type(mContentBean.getType());
            watchHistoryCountBody.setContent_id(mContentBean.getId());
            watchHistoryCountBody.setVideo_id(mContentPlaybackBean.getVideo_id());
            watchHistoryCountBody.setStart_time(mStartTime);
            watchHistoryCountBody.setEnd_time(endTime);
            mPresenter.addWatchHistoryCount(watchHistoryCountBody);
        }
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        boolean isSelected = mFavouriteBtn.isSelected();
        if (accountMessage != null && initFavourite != isSelected) {
            EditFavouriteReqBean editFavouriteReqBean = new EditFavouriteReqBean();
            editFavouriteReqBean.setContentid(mContentBean.getId());
            editFavouriteReqBean.setContenttype(mContentBean.getType());
            editFavouriteReqBean.setViewerid(accountMessage.getViewer_id());
            editFavouriteReqBean.setOperation(isSelected ? EditFavouriteReqBean.EDIT_MARK : EditFavouriteReqBean.EDIT_UNMARK);
            mPresenter.editFavourite(editFavouriteReqBean);
        }
        super.onDestroy();
    }

    @Override
    public void onContentPlaybackResponse(String writter, ContentResponseBean.SearchBean contentResponseBean, ContentPlaybackBean contentPlaybackBean) {
        mStartTime = sDateFormat.format(new Date());
        mContentPlaybackBean = contentPlaybackBean;
        mVodRecyclerViewAdapter.setWritter(writter);
        mProfileSettingAdapter.setVideoProfile(contentPlaybackBean.getVideo_profile());
        mPresenter.setTitleAndUrl(contentResponseBean.getTitle(), contentPlaybackBean.getVideo_profile().get(0).getUrl_http());
        mPresenter.preparePlayer();
    }

    @Override
    public void onCommentListResponse(ArrayList<CommentResult> comments) {
        mVodRecyclerViewAdapter.initAvailableWidth(mMessageAndCommentRv.getWidth());
        mVodRecyclerViewAdapter.setCommentData(comments);
    }

    @Override
    public void onWatchHistoryCount(int count) {
        mVodRecyclerViewAdapter.setViewerNumber(count);
    }

    @Override
    public void isFavourite(boolean favourite) {
        initFavourite = favourite;
        mFavouriteBtn.setSelected(initFavourite);
    }

    @Override
    public void onSuccess() {
        setLoadingVisibility(false);
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
                    mPresenter.switchPlayerSize(this, findViewById(R.id.top_view));
                } else {
                    finish();
                }
                break;
            case R.id.iv_download:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_favourite:
                LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
                if (accountMessage == null) {
                    DialogUtils.getAskLoginDialog(this).show();
                    break;
                }
                v.setSelected(!v.isSelected());
                break;
            case R.id.iv_settings:
                mProfileSettingRv.setVisibility(mProfileSettingRv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.exo_fullscreen:
                mPresenter.switchPlayerSize(this, findViewById(R.id.top_view));
                break;
        }
    }

    @Override
    public void commitComment(String commit) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage == null) {
            DialogUtils.getAskLoginDialog(this).show();
            return;
        }
        mPresenter.commitComment(mContentBean, commit);
        setLoadingVisibility(true);
    }

    @Override
    public void showMore() {

    }

    @Override
    public void longClickToDelete(CommentResult comment) {
        mPresenter.deleteComment(mContentBean, comment);
        setLoadingVisibility(true);
    }

    private void setLoadingVisibility(boolean visibility) {
        findViewById(R.id.progress_bar_layout).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onProfileSelect(ContentPlaybackBean.VideoProfileBean videoProfileBean) {
        mPresenter.setTitleAndUrl(mContentBean.getTitle(), videoProfileBean.getUrl_http());
        mPresenter.preparePlayer(true);
        mProfileSettingRv.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPresenter.isFullScreen() && (event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE || event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            mPresenter.switchPlayerSize(this, findViewById(R.id.top_view));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
