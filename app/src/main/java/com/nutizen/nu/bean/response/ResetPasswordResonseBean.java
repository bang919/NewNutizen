package com.nutizen.nu.bean.response;

import java.io.Serializable;
import java.util.List;


public class ResetPasswordResonseBean implements Serializable {


    /**
     * status : true
     */

    public boolean status;

    /**
     * code : 400
     * message : ["email is invalid"]
     */

    public ErrBean err;

    public static class ErrBean {
        public int code;
        public List<String> message;
    }
}
