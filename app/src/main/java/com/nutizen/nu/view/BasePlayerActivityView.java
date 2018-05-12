package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.CommentResult;

import java.util.ArrayList;

public interface BasePlayerActivityView {

    void onCommentListResponse(ArrayList<CommentResult> comments);

    void onSuccess();

    void onFailure(String errorMsg);
}
