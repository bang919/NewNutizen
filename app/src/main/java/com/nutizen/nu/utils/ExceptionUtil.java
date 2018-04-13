package com.nutizen.nu.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.nutizen.nu.bean.response.ErrorResponseBean;
import com.nutizen.nu.bean.response.ErrorResponseBean2;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/11/8.
 */

public class ExceptionUtil {

    private static Gson gson = new Gson();

    public static String getHttpExceptionMessage(Throwable t) {
        if (t instanceof HttpException) {
            ResponseBody body = ((HttpException) t).response().errorBody();
            String bodyString = null;
            try {
                bodyString = body.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ErrorResponseBean errorResBean = gson.fromJson(bodyString, ErrorResponseBean.class);
                List<String> messages = errorResBean.getErr().getMessage();
                StringBuilder returnMessage = new StringBuilder();
                for (String msg : messages) {
                    returnMessage.append(msg + ";");
                }
                returnMessage.deleteCharAt(returnMessage.length() - 1);
                return returnMessage.toString();
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    ErrorResponseBean2 errorResBean2 = gson.fromJson(bodyString, ErrorResponseBean2.class);
                    return errorResBean2.getErr().getMessage();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    try {//直接返回body
                        if (!TextUtils.isEmpty(body.string())) {
                            return body.string();
                        }
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        return t.getLocalizedMessage();
    }
}
