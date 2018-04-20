package com.nutizen.nu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.VodRecyclerViewAdapter;
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

import java.util.ArrayList;

public class ContentPlayerActivity extends BaseActivity<ContentPlayerPresenter> implements ContentPlayerView, View.OnClickListener, VodRecyclerViewAdapter.CommentAdapterCallback {

    public static final String CONTENT_BEAN = "content bean";
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private RecyclerView mMessageAndCommentRv;
    private VodRecyclerViewAdapter mVodRecyclerViewAdapter;
    private ContentResponseBean.SearchBean mContentBean;

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
    }

    @Override
    protected void initEvent() {
        mSimpleExoPlayerView.findViewById(R.id.exo_fullscreen).setOnClickListener(this);
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
    public void onContentPlaybackResponse(String writter, ContentResponseBean.SearchBean contentResponseBean, ContentPlaybackBean contentPlaybackBean) {
        mVodRecyclerViewAdapter.setWritter(writter);
        mPresenter.setUrl(contentPlaybackBean.getVideo_profile().get(0).getUrl_http());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPresenter.isFullScreen() && (event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE || event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            mPresenter.switchPlayerSize(this, findViewById(R.id.top_view));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
