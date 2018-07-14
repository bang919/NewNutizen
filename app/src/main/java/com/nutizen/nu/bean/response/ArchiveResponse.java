package com.nutizen.nu.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ArchiveResponse {

    /**
     * path : /var/leto/nutizen/testArchive-1494308352
     * success : true
     * err : {"code":400,"message":["cannot archive"]}
     */

    private String path;
    private boolean success;
    private boolean status;
    private ErrBean err;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ErrBean getErr() {
        return err;
    }

    public void setErr(ErrBean err) {
        this.err = err;
    }

    public static class ErrBean {
        /**
         * code : 400
         * message : ["cannot archive"]
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
