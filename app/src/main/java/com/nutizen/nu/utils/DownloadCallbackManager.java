package com.nutizen.nu.utils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.TreeMap;

/**
 * 用于Download的回调处理，和DownloadingListAdapter联动
 */
public class DownloadCallbackManager {

    private final static class HolderClass {
        private final static DownloadCallbackManager INSTANCE = new DownloadCallbackManager();
    }

    public static DownloadCallbackManager getImpl() {
        return HolderClass.INSTANCE;
    }

    // 可以考虑与url绑定?与id绑定?
    private TreeMap<String, DownloadStatusUpdater> updaterMap = new TreeMap<>();

    public void startDownload(final String url, final String path) {
        FileDownloader.getImpl().create(url)
                .setPath(path)
                .setListener(lis)
                .start();
    }

    public void addUpdater(String downloadUrl, final DownloadStatusUpdater updater) {
        updaterMap.put(downloadUrl, updater);
    }

    public void removeUpdater(String downloadUrl) {
        updaterMap.remove(downloadUrl);
    }

    public void clearUpdaters() {
        updaterMap.clear();
    }


    private FileDownloadListener lis = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            returnUpdate(task);
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            returnUpdate(task);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            returnUpdate(task);
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
            retruenComplete(task);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            retruenComplete(task);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            returnUpdate(task);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            returnUpdate(task);
        }

        @Override
        protected void warn(BaseDownloadTask task) {
            returnUpdate(task);
        }
    };

    private void retruenComplete(BaseDownloadTask task) {
        String url = task.getUrl();
        DownloadStatusUpdater downloadStatusUpdater = updaterMap.get(url);
        if (downloadStatusUpdater != null) {
            downloadStatusUpdater.complete(task);
        }
    }

    private void returnUpdate(final BaseDownloadTask task) {
        String url = task.getUrl();
        DownloadStatusUpdater downloadStatusUpdater = updaterMap.get(url);
        if (downloadStatusUpdater != null) {
            downloadStatusUpdater.update(task);
        }
    }

    public interface DownloadStatusUpdater {
        void complete(BaseDownloadTask task);

        void update(BaseDownloadTask task);
    }

}