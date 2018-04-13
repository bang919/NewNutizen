package com.nutizen.nu.bean.response;

import java.io.Serializable;

public class ForgetPasswordResponse implements Serializable {


    /**
     * status : true
     */

    public boolean status;


    /**
     * code : 400
     * message : invalid code
     */

    public ErrBean err;

    public static class ErrBean {
        public int code;
        public String message;
    }

    @Override
    public String toString() {
        return "ForgetResonse{" +
                "status=" + status +
                ", err=" + err +
                '}';
    }
}
