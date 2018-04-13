package com.nutizen.nu.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ErrorResponseBean {


    /**
     * err : {"code":400,"message":["please input username or email","please input password"]}
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
         * message : ["please input username or email","please input password"]
         */

        private int code;
        private List<String> message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<String> getMessage() {
            return message;
        }

        public void setMessage(List<String> message) {
            this.message = message;
        }
    }
}
