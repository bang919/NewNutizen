package com.nutizen.nu.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.nutizen.nu.R;
import com.nutizen.nu.adapter.DownloadViewPagerAdapter;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.DownloadDatabaseUtil;
import com.nutizen.nu.utils.DownloadCallbackManager;
import com.nutizen.nu.utils.FileUtils;
import com.nutizen.nu.utils.ToastUtils;

import java.util.ArrayList;

public class DownloadFragment extends BaseDialogFragment implements View.OnClickListener, DownloadViewPagerAdapter.DownloadViewPagerAdapterListener {

    public static final String TAG = "DownloadFragment";

    private View mBackBtn;
    private TextView mTitleTv;
    private TextView mCancelBtn, mEditBtn, mDoneBtn;
    private ViewGroup mDownloadingLayout, mDownloadedLayout;
    private ViewPager mViewPager;
    private View mProgressView;
    private DownloadViewPagerAdapter mDownloadViewPagerAdapter;

    public static DownloadFragment getInstance() {
        return new DownloadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //如果按退出键的时候是Editing状态，取消Editing状态
                if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) && event.getAction() == KeyEvent.ACTION_DOWN && mCancelBtn.getVisibility() == View.VISIBLE) {
                    setEditStatus(false);
                    return true;
                }
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_download;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mBackBtn = rootView.findViewById(R.id.back);
        mTitleTv = rootView.findViewById(R.id.title_text_title_fragment);
        mCancelBtn = rootView.findViewById(R.id.btn_cancel);
        mEditBtn = rootView.findViewById(R.id.btn_edit);
        mDoneBtn = rootView.findViewById(R.id.btn_remove);
        mDownloadingLayout = rootView.findViewById(R.id.layout_downloading);
        mDownloadedLayout = rootView.findViewById(R.id.layout_downloaded);
        mViewPager = rootView.findViewById(R.id.download_viewpager);
        mProgressView = rootView.findViewById(R.id.progress_bar_layout);
        ((TextView) mDownloadedLayout.findViewById(R.id.text_downloading)).setText(R.string.downloaded);
        switchToSubTitle(false);
        if (mTitleTv != null) {
            mTitleTv.setText(getString(R.string.download_title));
        }
    }

    /**
     * 最上面Downloading/Downloaded按钮的UI
     *
     * @param toDownloaded true为downloaded，false为默认的downloading
     */
    private void switchToSubTitle(boolean toDownloaded) {
        setEditStatus(false);
        int downloadingChildViewCount = mDownloadingLayout.getChildCount();
        mDownloadingLayout.setSelected(!toDownloaded);
        for (int i = 0; i <= downloadingChildViewCount - 1; i++) {
            View childView = mDownloadingLayout.getChildAt(0);
            childView.setSelected(!toDownloaded);
        }
        int downloadedChildViewCount = mDownloadedLayout.getChildCount();
        mDownloadedLayout.setSelected(toDownloaded);
        for (int i = 0; i <= downloadedChildViewCount - 1; i++) {
            View childView = mDownloadedLayout.getChildAt(0);
            childView.setSelected(toDownloaded);
        }
        mViewPager.setCurrentItem(toDownloaded ? 1 : 0);
    }


    @Override
    protected void initEvent() {
        mBackBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
        mDoneBtn.setOnClickListener(this);
        mDownloadingLayout.setOnClickListener(this);
        mDownloadedLayout.setOnClickListener(this);

        mDownloadViewPagerAdapter = new DownloadViewPagerAdapter(getContext(), this);
        mViewPager.setAdapter(mDownloadViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchToSubTitle(position != 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    setEditStatus(false);
                }
            }
        });

        setEditStatus(false);
        requestData();
    }

    @Override
    public void onDestroy() {
        DownloadCallbackManager.getImpl().clearUpdaters();
        super.onDestroy();
    }

    @Override
    public void onDownloadCompleteData() {
        requestData();
    }

    private void requestData() {
        setProgressVisibility(true);
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().addServiceConnectListener(new FileDownloadConnectListener() {
                @Override
                public void connected() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            requestData();
                        }
                    });
                }

                @Override
                public void disconnected() {
                    ToastUtils.showShort("FileDownloader disconnected");
                }
            });
            FileDownloader.getImpl().bindService();
            return;
        }
        setProgressVisibility(false);
        ArrayList<ContentResponseBean.SearchBean> downloadBeans = DownloadDatabaseUtil.listDownloadContents(getContext());
        ArrayList<ContentResponseBean.SearchBean> downloadingBeans = new ArrayList<>();
        ArrayList<ContentResponseBean.SearchBean> downloadedBeans = new ArrayList<>();
        for (ContentResponseBean.SearchBean bean : downloadBeans) {
            int status = FileDownloader.getImpl().getStatus(bean.getStoreDownloadUrl(), FileUtils.getFileDownloaderFilePath(getContext(), bean.getDownloadFileName()));
            if (status == FileDownloadStatus.completed) {
                downloadedBeans.add(bean);
            } else {
                downloadingBeans.add(bean);
            }
        }
        mDownloadViewPagerAdapter.setDownloadingData(downloadingBeans);
        mDownloadViewPagerAdapter.setDownloadedData(downloadedBeans);
        ((TextView) mDownloadingLayout.findViewById(R.id.count_downloading)).setText(String.valueOf(downloadingBeans.size()));
        ((TextView) mDownloadedLayout.findViewById(R.id.count_downloading)).setText(String.valueOf(downloadedBeans.size()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.btn_edit:
                setEditStatus(true);
                break;
            case R.id.btn_remove:
                removeDownloadSelected();
                break;
            case R.id.btn_cancel:
                setEditStatus(false);
                break;
            case R.id.layout_downloading:
                switchToSubTitle(false);
                break;
            case R.id.layout_downloaded:
                switchToSubTitle(true);
                break;
        }
    }

    private void setProgressVisibility(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setEditStatus(boolean edit) {
        if (mCancelBtn.getVisibility() == (edit ? View.VISIBLE : View.GONE)) {//判断是否已经是edit了，是的话就返回不继续了
            return;
        }
        if (mDownloadViewPagerAdapter != null) {
            mDownloadViewPagerAdapter.changeEditStatus(mViewPager.getCurrentItem(), edit);
        }

        mCancelBtn.setVisibility(edit ? View.VISIBLE : View.GONE);
        mDoneBtn.setVisibility(edit ? View.VISIBLE : View.GONE);
        mEditBtn.setVisibility(edit ? View.GONE : View.VISIBLE);
        mBackBtn.setVisibility(edit ? View.GONE : View.VISIBLE);
    }

    private void removeDownloadSelected() {
        mDownloadViewPagerAdapter.removeDownloadSelected(getContext(), mViewPager.getCurrentItem());
        requestData();
        setEditStatus(false);
    }
}
