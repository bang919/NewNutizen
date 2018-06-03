package com.nutizen.nu.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.ContentPlayerAdapter;
import com.nutizen.nu.adapter.ProfileSettingAdapter;
import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.request.WatchHistoryCountBody;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.presenter.ContentPlayerActivityPresenter;
import com.nutizen.nu.utils.DownloadDatabaseUtil;
import com.nutizen.nu.view.ContentPlayerActivityView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContentPlayerActivity extends PlayerActivity<ContentResponseBean.SearchBean, ContentPlayerActivityPresenter> implements ContentPlayerActivityView, ProfileSettingAdapter.OnProfileSelectListener {

    private ContentPlaybackBean mContentPlaybackBean;

    private View mDownloadBtn;
    private RecyclerView mProfileSettingRv;
    private ProfileSettingAdapter mProfileSettingAdapter;

    private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String mStartTime;//用于addWatchCount

    public static void startContentPlayActivity(Context context, ContentResponseBean.SearchBean searchBean) {
        Intent intent = new Intent(context, ContentPlayerActivity.class);
        intent.putExtra(ContentPlayerActivity.DATA_BEAN, searchBean);
        context.startActivity(intent);
    }

    @Override
    protected ContentPlayerActivityPresenter initPresenter() {
        return new ContentPlayerActivityPresenter(this, this);
    }

    @Override
    protected void initView() {
        super.initView();
        mProfileSettingRv = findViewById(R.id.rv_profile_settings);
        mDownloadBtn = mSimpleExoPlayerView.findViewById(R.id.iv_download);
        View settingsBtn = mSimpleExoPlayerView.findViewById(R.id.iv_settings);
        mDownloadBtn.setVisibility(View.VISIBLE);
        settingsBtn.setVisibility(View.VISIBLE);
        mDownloadBtn.setOnClickListener(this);
        settingsBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        mProfileSettingAdapter = new ProfileSettingAdapter();
        mProfileSettingAdapter.setOnProfileSelectListener(this);
        mProfileSettingRv.setAdapter(mProfileSettingAdapter);
        mProfileSettingRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected ContentResponseBean.SearchBean setDataBean() {
        return (ContentResponseBean.SearchBean) getIntent().getSerializableExtra(DATA_BEAN);
    }

    @Override
    protected String returnCommentType() {
        return mData.getType();
    }

    @Override
    protected int returnCommentId() {
        return mData.getId();
    }


    @Override
    protected ContentPlayerAdapter createBasePlayerAdapter() {
        return new ContentPlayerAdapter(mData);
    }

    @Override
    protected View setFavouriteBtn() {
        return findViewById(R.id.iv_favourite);
    }

    @Override
    protected void editFavourite(EditFavouriteReqBean editFavouriteReqBean) {
        mPresenter.editFavourite(editFavouriteReqBean);
    }

    @Override
    public void onContentPlaybackResponse(ContentResponseBean.SearchBean contentResponseBean, ContentPlaybackBean contentPlaybackBean) {
        mStartTime = sDateFormat.format(new Date());
        mContentPlaybackBean = contentPlaybackBean;
        ((ContentPlayerAdapter) mBasePlayerAdapter).setWritter(contentResponseBean.getWritter());
        mProfileSettingAdapter.setVideoProfile(contentPlaybackBean.getVideo_profile());
        ArrayList<ContentPlaybackBean.VideoProfileBean> profiles = contentPlaybackBean.getVideo_profile();
        //是否在下载
        mDownloadBtn.setSelected(DownloadDatabaseUtil.checkDownloaded(this, contentPlaybackBean));
        mPresenter.setTitleAndUrl(contentResponseBean.getTitle(), profiles.get(0).getUrl_http());
        mPresenter.preparePlayer();
    }

    @Override
    public void onWatchHistoryCount(int count) {
        ((ContentPlayerAdapter) mBasePlayerAdapter).setViewerNumber(count);
    }


    @Override
    public void isFavourite(boolean favourite) {
        setIsFavourite(favourite);
    }

    @Override
    public void onProfileSelect(ContentPlaybackBean.VideoProfileBean videoProfileBean) {
        mPresenter.setTitleAndUrl(mData.getTitle(), videoProfileBean.getUrl_http());
        mPresenter.preparePlayer(true);
        mProfileSettingRv.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        String endTime = sDateFormat.format(new Date());
        if (mContentPlaybackBean != null && mStartTime != null) {
            WatchHistoryCountBody watchHistoryCountBody = new WatchHistoryCountBody();
            watchHistoryCountBody.setContent_name(mData.getTitle());
            watchHistoryCountBody.setContent_type(mData.getType());
            watchHistoryCountBody.setContent_id(mData.getId());
            watchHistoryCountBody.setVideo_id(mContentPlaybackBean.getVideo_id());
            watchHistoryCountBody.setStart_time(mStartTime);
            watchHistoryCountBody.setEnd_time(endTime);
            mPresenter.addWatchHistoryCount(watchHistoryCountBody);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_download:
                boolean isDownload = mPresenter.switchDownloading(mPresenter.getCurrentUrl(), mContentPlaybackBean, getDataBean());
                mDownloadBtn.setSelected(isDownload);
                break;
            case R.id.iv_settings:
                mProfileSettingRv.setVisibility(mProfileSettingRv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
        }
    }


}
