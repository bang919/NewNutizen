package com.nutizen.nu.http;


import com.nutizen.nu.BuildConfig;
import com.nutizen.nu.bean.request.CommentBean;
import com.nutizen.nu.bean.request.EditFavouriteReqBean;
import com.nutizen.nu.bean.request.LoginRequestBean;
import com.nutizen.nu.bean.request.ViewDetailReqBean;
import com.nutizen.nu.bean.request.WatchHistoryCountBody;
import com.nutizen.nu.bean.response.AdvertisementBean;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentPlaybackBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.ContributorContentResult;
import com.nutizen.nu.bean.response.ContributorLiveResult;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.bean.response.ForgetPasswordResponse;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.NormalResBean;
import com.nutizen.nu.bean.response.RegisterResponse;
import com.nutizen.nu.bean.response.ResetPasswordResonseBean;
import com.nutizen.nu.bean.response.WatchHistoryCountRes;
import com.nutizen.nu.bean.third.FacebookSdkBean;
import com.nutizen.nu.bean.third.RegisterFacebookRspBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/17.
 */

public interface ApiInterface {

    @Streaming
    @GET
        //下载文件
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&type=graphics_advertisement")
    Observable<AdvertisementBean> getAdvertisement();

    @POST("viewers/login")
    Observable<LoginResponseBean> login(@Body LoginRequestBean loginRequestBean);

    @GET("viewers")
    Observable<LoginResponseBean.DetailBean> getViewerDetail(@Header("Authorization") String contentRange);

    @PUT("viewers")
    Observable<NormalResBean> updateViewerDetail(@Body ViewDetailReqBean body, @Header("Authorization") String token);

    @Multipart
    @Headers({"mimetype:image/jpeg"})
    @POST("viewers/picture")
    Observable<NormalResBean> uploadPortrait(@Part MultipartBody.Part file, @Header("Authorization") String token);

    @POST("viewers/reset")
    Observable<ResetPasswordResonseBean> searchEmailToRestPassword(@Body RequestBody body);

    @POST("viewers/forgetpassword")
    Observable<ForgetPasswordResponse> forgetpassword(@Body RequestBody body);

    @POST("viewers/register")
    Observable<RegisterResponse> registerAccount(@Body RequestBody body);

    @POST("viewers/register/third")
    Observable<RegisterFacebookRspBean> registerByFacebook(@Body FacebookSdkBean body);

    @POST("viewers/login/third")
    Observable<LoginResponseBean> loginByFacebook(@Body FacebookSdkBean body);

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&c=channels&type=movie")
    Observable<ContentResponseBean> requestHomeBanner();

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&c=editors&type=movie")
    Observable<ContentResponseBean> requestEditors();

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&type=movie")
    Observable<ContentResponseBean> requestNewly(@Query("limit") int limit, @Query("offset") int offset);

    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("contributors/movie")
    Observable<ContributorContentResult> searchContentByContributor(@Query("vid") Integer vid, @Query("limit") Integer limit, @Query("next") Integer nextId);

    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("contributors/live")
    Observable<ContributorLiveResult> searchLiveByContributor(@Query("vid") Integer vid, @Query("status") Integer status, @Query("limit") Integer limit, @Query("next") Integer nextId);

    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("live?status=1")
    Observable<ArrayList<LiveResponseBean>> requestLive();

    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("live?type=rtsp")
    Observable<ArrayList<LiveResponseBean>> requestTvAndRadio();

    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("contributors?limit=99999999")
    Observable<KanalRspBean> getKanal(@Query("filename") String filename);

    //SearchActivity，标题搜索
    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("contents/movie")
    Observable<ContentResponseBean> searchMovieByTitle(@Query("filename") String title, @Query("limit") int limit, @Query("next") Integer nextId);

    // 获取content对应video id
    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&type=movie")
    Observable<ContentResponseBean> getVideoIdByContentId(@Query("id") int id);

    // 获取video播放地址
    @GET(BuildConfig.playurl_hostname + "videos/{vid}")
    Observable<ContentPlaybackBean> getPlaybackInfoByVideoId(@Path("vid") int id);

    @POST("watchhistories/offline")
    Observable<WatchHistoryCountRes> addOfflineWatchHistoryCount(@Header("Authorization") String token, @Body WatchHistoryCountBody watchHistoryCountBody);

    // 用contentid获取历史播放数量
    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("watchhistories/count")
    Observable<WatchHistoryCountRes> getWatchHistoryCount(@Query("type") String type, @Query("id") int cid, @Query("from") String startTime, @Query("to") String endTime);

    @GET(BuildConfig.comment_url)
    Observable<ArrayList<CommentResult>> getCommentList(@Query("ctype") String ctype, @Query("cid") int cid, @Query("page") int page, @Query("limit") int limit, @Query("sort") int sort);

    @POST(BuildConfig.comment_url)
    Observable<CommentResult> sendComment(@Header("Authorization") String token, @Body CommentBean commentBean);

    @HTTP(method = "DELETE", path = BuildConfig.comment_url, hasBody = true)
    Observable<CommentResult> deleteComment(@Header("Authorization") String token, @Body RequestBody commentId);

    /**
     * 添加喜爱
     */
    @POST(BuildConfig.data_hostname + "devapi/editFavor?apikey=" + BuildConfig.server_key)
    Observable<NormalResBean> editFavourite(@Body EditFavouriteReqBean editFavourite);

    /**
     * 获取喜爱列表
     */
    @GET("viewers/favourites")
    Observable<ArrayList<FavouriteRspBean>> getFavourites(@Header("Authorization") String token);
}
