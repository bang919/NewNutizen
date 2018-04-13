package com.nutizen.nu.http;


import com.nutizen.nu.BuildConfig;
import com.nutizen.nu.bean.request.LoginRequestBean;
import com.nutizen.nu.bean.response.AdvertisementBean;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.ForgetPasswordResponse;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.bean.response.RegisterResponse;
import com.nutizen.nu.bean.response.ResetPasswordResonseBean;
import com.nutizen.nu.bean.third.FacebookSdkBean;
import com.nutizen.nu.bean.third.LoginFacebookRspBean;
import com.nutizen.nu.bean.third.RegisterFacebookRspBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @POST("viewers/reset")
    Observable<ResetPasswordResonseBean> searchEmailToRestPassword(@Body RequestBody body);

    @POST("viewers/forgetpassword")
    Observable<ForgetPasswordResponse> forgetpassword(@Body RequestBody body);

    @POST("viewers/register")
    Observable<RegisterResponse> registerAccount(@Body RequestBody body);

    @POST("viewers/register/third")
    Observable<RegisterFacebookRspBean> registerByFacebook(@Body FacebookSdkBean body);

    @POST("viewers/login/third")
    Observable<LoginFacebookRspBean> loginByFacebook(@Body FacebookSdkBean body);

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&c=channels&type=movie")
    Observable<ContentResponseBean> requestHomeBanner();

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&c=editors&type=movie")
    Observable<ContentResponseBean> requestEditors();

    @GET(BuildConfig.data_hostname + "devapi/?apikey=" + BuildConfig.server_key + "&type=movie")
    Observable<ContentResponseBean> requestNewly(@Query("limit") int limit, @Query("offset") int offset);

    @Headers("Authorization:Bearer " + BuildConfig.server_key)
    @GET("live?status=1")
    Observable<ArrayList<LiveResponseBean>> requestLive();
}
