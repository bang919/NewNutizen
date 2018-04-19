package com.nutizen.nu.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.VodRecyclerViewAdapter;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.ContentPlayerPresenter;
import com.nutizen.nu.view.ContentPlayerView;

import java.util.ArrayList;

public class ContentPlayerActivity extends BaseActivity<ContentPlayerPresenter> implements ContentPlayerView, View.OnClickListener {

    public static final String CONTENT_BEAN = "content bean";
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private RecyclerView mMessageAndCommentRv;
    private VodRecyclerViewAdapter mVodRecyclerViewAdapter;

    @Override
    protected int getBarColor() {
        return R.color.colorBlack;
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
        Intent intent = getIntent();
        ContentResponseBean.SearchBean contentBean = (ContentResponseBean.SearchBean) intent.getSerializableExtra(CONTENT_BEAN);
        mPresenter.getDatas(contentBean);

        mVodRecyclerViewAdapter = new VodRecyclerViewAdapter(this, contentBean);
        mMessageAndCommentRv.setAdapter(mVodRecyclerViewAdapter);
        mMessageAndCommentRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        mSimpleExoPlayerView.findViewById(R.id.exo_fullscreen).setOnClickListener(this);
    }

    @Override
    public void onContentPlaybackResponse(String writter, ContentResponseBean.SearchBean contentResponseBean, ContentPlaybackBean contentPlaybackBean) {
        mVodRecyclerViewAdapter.setWritter(writter);
        Log.d("bigbang", "url ---- " + contentPlaybackBean.getVideo_profile().get(0).getUrl_http());

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

    }

    @Override
    public void onFailure(String errorMsg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exo_fullscreen:
                break;
        }
    }
}
