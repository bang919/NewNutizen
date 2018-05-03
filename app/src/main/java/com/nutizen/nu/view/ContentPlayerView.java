package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;

import java.util.ArrayList;

public interface ContentPlayerView {
    void onContentPlaybackResponse(ContentResponseBean.SearchBean contentResponseBean, ContentPlaybackBean contentPlaybackBean);

    void onCommentListResponse(ArrayList<CommentResult> comments);

    void onWatchHistoryCount(int count);

    void isFavourite(boolean favourite);

    void onSuccess();

    void onFailure(String errorMsg);
}
