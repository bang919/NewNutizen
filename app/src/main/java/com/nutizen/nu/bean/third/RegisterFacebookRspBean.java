package com.nutizen.nu.bean.third;

/**
 * Created by bigbang on 2018/3/30.
 */

public class RegisterFacebookRspBean {
    /**
     * err : {"code":400,"message":"Username or Email exists"}
     * status : true
     */

    private ErrBean err;
    private boolean status;

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class ErrBean {
        /**
         * code : 400
         * message : Username or Email exists
         */

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
