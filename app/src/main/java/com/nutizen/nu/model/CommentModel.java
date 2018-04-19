package com.nutizen.nu.model;

import com.nutizen.nu.bean.request.CommentBean;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.http.HttpClient;
import com.nutizen.nu.presenter.LoginPresenter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CommentModel {

    public Observable<ArrayList<CommentResult>> getCommentList(String type, int cid, int page, int limit, int sort) {
        return HttpClient.getApiInterface()
                .getCommentList(type, cid, page, limit, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommentResult> sendComment(CommentBean commentBean) {
        String token = LoginPresenter.getAccountMessage().getToken();
        return HttpClient.getApiInterface()
                .sendComment("bearer " + token, commentBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommentResult> deleteComment(CommentResult commentResult) {
        String token = LoginPresenter.getAccountMessage().getToken();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{\"comment_id\":\"" + commentResult.get_id() + "\"}");
        return HttpClient.getApiInterface()
                .deleteComment("bearer " + token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
