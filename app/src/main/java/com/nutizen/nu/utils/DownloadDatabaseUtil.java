package com.nutizen.nu.utils;

import android.content.Context;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;

import java.io.File;
import java.util.ArrayList;

/**
 * 用户Download的增删改查
 */
public class DownloadDatabaseUtil {

    private static final String DOWNLOAD_CONTENTS = "download contents";

    /**
     * 查看是否已经在下载或已下载
     */
    public static boolean checkDownloaded(Context context, ContentPlaybackBean contentPlaybackBean) {
        String downloadPath = FileUtils.getFileDownloaderFilePath(context, contentPlaybackBean.getDownloadFileName());
        ArrayList<ContentPlaybackBean.VideoProfileBean> profiles = contentPlaybackBean.getVideo_profile();
        boolean isDownloading = false;
        for (ContentPlaybackBean.VideoProfileBean videoProfileBean : profiles) {
            String downloadUrl = getDownloadUrl(videoProfileBean.getUrl_http());
            int status = FileDownloader.getImpl().getStatus(downloadUrl, downloadPath);
            if (status == FileDownloadStatus.error) {
                isDownloading |= false;
            } else if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                    status == FileDownloadStatus.connected || status == FileDownloadStatus.progress || status == FileDownloadStatus.completed) {
                isDownloading |= true;
            } else {
                isDownloading |= false;
            }
        }
        return isDownloading;
    }

    public static String getDownloadUrl(String playbackUrl) {
        String downloadUrl = playbackUrl.replace("/hls/", "/").replace(".m3u8", ".mp4");
        if (downloadUrl.contains("default")) {
            downloadUrl = downloadUrl.replace("default", "default/mp4");
        }
        return downloadUrl;
    }


    /**
     * ------------------------------- 增删改查 -------------------------------------
     */
    public static void addDownloadContent(Context context, ContentResponseBean.SearchBean contentBean, String downloadUrl, String downloadFileName) {
        //add FileDownloader
        String downloadPath = FileUtils.getFileDownloaderFilePath(context, downloadFileName);
        DownloadCallbackManager.getImpl().startDownload(downloadUrl, downloadPath);

        //add SharedPreferences
        ArrayList<ContentResponseBean.SearchBean> contents = listDownloadContents(context);
        if (contents == null) {
            contents = new ArrayList<>();
        }
        contentBean.setStoreDownloadUrl(downloadUrl);
        contentBean.setDownloadFileName(downloadFileName);
        contents.add(contentBean);
        SPUtils.putObject(context, DOWNLOAD_CONTENTS, contents);
    }

    public static ArrayList<ContentResponseBean.SearchBean> listDownloadContents(Context context) {
        try {
            return SPUtils.getObject(context, DOWNLOAD_CONTENTS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContentResponseBean.SearchBean searchDownloadBeanByDownloadFileName(Context context, String downloadFileName) {
        ArrayList<ContentResponseBean.SearchBean> beans = listDownloadContents(context);
        if (beans != null) {
            for (ContentResponseBean.SearchBean bean : beans) {
                if (bean.getDownloadFileName().equals(downloadFileName)) {
                    return bean;
                }
            }
        }
        return null;
    }

    public static void removeDownloadBeanByDownloadFileName(Context context, String downloadFileName) {
        //清理SharedPreferences
        ArrayList<ContentResponseBean.SearchBean> beans = listDownloadContents(context);
        if (beans != null) {
            for (int i = beans.size() - 1; i >= 0; i--) {
                ContentResponseBean.SearchBean bean = beans.get(i);
                if (bean.getDownloadFileName().equals(downloadFileName)) {
                    //清理FileDownloader
                    String downloadPath = FileUtils.getFileDownloaderFilePath(context, downloadFileName);
                    BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(bean.getStoreDownloadUrl()).setPath(downloadPath);
                    baseDownloadTask.pause();
                    FileDownloader.getImpl().clear(baseDownloadTask.getId(), baseDownloadTask.getTargetFilePath());

                    beans.remove(i);
                }
            }
            SPUtils.putObject(context, DOWNLOAD_CONTENTS, beans);
        }

        //清理文件folder
        File folder = new File(FileUtils.getFileDownloaderDir(context));
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.getPath().contains(downloadFileName)) {
                        file1.delete();
                    }
                }
            }
        }
    }
}
