package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.utils.DownloadCallbackManager;
import com.nutizen.nu.utils.FileUtils;

import java.util.Locale;

public class DownlaodingListAdapter extends DownloadListAdapter {

    private DownloadingListAdapterListener mDownloadingListAdapterListener;

    public DownlaodingListAdapter(DownloadingListAdapterListener listAdapterListener) {
        super();
        mDownloadingListAdapterListener = listAdapterListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final DownloadViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        //进度的回调处理
        final ContentResponseBean.SearchBean data = mDatas.get(position);
        final DownloadCallbackManager.DownloadStatusUpdater updater = new DownloadCallbackManager.DownloadStatusUpdater() {
            @Override
            public void complete(BaseDownloadTask task) {
                DownloadCallbackManager.getImpl().removeUpdater(data.getStoreDownloadUrl());
                if (mDownloadingListAdapterListener != null) {
                    mDownloadingListAdapterListener.onCompleteOne(task);
                }
            }

            @Override
            public void update(BaseDownloadTask task) {
                setProgress(holder, task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes());
            }
        };
        DownloadCallbackManager.getImpl().addUpdater(data.getStoreDownloadUrl(), updater);
        //进度的更改初始UI
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(data.getStoreDownloadUrl()).setPath(FileUtils.getFileDownloaderFilePath(holder.itemView.getContext(), data.getDownloadFileName()));
        int id = baseDownloadTask.getId();
        byte status = FileDownloader.getImpl().getStatus(id, FileUtils.getFileDownloaderFilePath(holder.itemView.getContext(), data.getDownloadFileName()));
        holder.mPlayBtn.setSelected(status > 0);
        setProgress(holder, FileDownloader.getImpl().getSoFar(id), FileDownloader.getImpl().getTotal(id));
    }

    @Override
    public void onItemClick(Context context, DownloadViewHolder holder, ContentResponseBean.SearchBean bean) {
        BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(bean.getStoreDownloadUrl()).setPath(FileUtils.getFileDownloaderFilePath(holder.itemView.getContext(), bean.getDownloadFileName()));
        int id = baseDownloadTask.getId();
        byte status = FileDownloader.getImpl().getStatus(id, FileUtils.getFileDownloaderFilePath(holder.itemView.getContext(), bean.getDownloadFileName()));
        if (status > 0) {//正在下载
            FileDownloader.getImpl().pause(id);
            holder.mPlayBtn.setSelected(false);
        } else {//没有在下载
            DownloadCallbackManager.getImpl().startDownload(bean.getStoreDownloadUrl(), FileUtils.getFileDownloaderFilePath(holder.itemView.getContext(), bean.getDownloadFileName()));
            holder.mPlayBtn.setSelected(true);
        }
    }

    private void setProgress(@NonNull DownloadViewHolder holder, long sofar, long total) {
        if (total == 0) {
            holder.mProgressBar.setProgress(0);
            holder.mStateTv.setText(R.string.waiting);
            return;
        }
        holder.mProgressBar.setProgress((int) (sofar * 100 / total));
        holder.mStateTv.setText(String.format(Locale.getDefault(), "%dmb / %dmb",
                sofar / 1000 / 1000, total / 1000 / 1000));
    }

    public interface DownloadingListAdapterListener {
        void onCompleteOne(BaseDownloadTask task);
    }
}
