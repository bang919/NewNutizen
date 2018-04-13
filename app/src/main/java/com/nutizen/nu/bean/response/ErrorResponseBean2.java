package com.nutizen.nu.bean.response;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ErrorResponseBean2 {

    /**
     * err : {"code":400,"message":"Username or password error."}
     */

    private ErrBean err;

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public static class ErrBean {
        /**
         * code : 400
         * message : Username or password error.
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
