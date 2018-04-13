package com.nutizen.nu.bean.third;

/**
 * Created by bigbang on 2018/3/30.
 */

public class LoginFacebookRspBean {

    /**
     * status : true
     * viewer_id : 38915
     * viewer_token : 7ecec7562078f8cf3addc187652b2b55a3a34599a612eef254039688d48cad9b
     * viewer_token_expiry_date : 1522406854
     * viewer_static_token : fd2a430c8b9c8db9598407dc226b6cb1a2ab30f854f42791c177ea01add46a0c
     * viewer_static_token_expiry_date : 1525081654
     */

    private boolean status;
    private int viewer_id;
    private String viewer_token;
    private int viewer_token_expiry_date;
    private String viewer_static_token;
    private int viewer_static_token_expiry_date;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getViewer_id() {
        return viewer_id;
    }

    public void setViewer_id(int viewer_id) {
        this.viewer_id = viewer_id;
    }

    public String getViewer_token() {
        return viewer_token;
    }

    public void setViewer_token(String viewer_token) {
        this.viewer_token = viewer_token;
    }

    public int getViewer_token_expiry_date() {
        return viewer_token_expiry_date;
    }

    public void setViewer_token_expiry_date(int viewer_token_expiry_date) {
        this.viewer_token_expiry_date = viewer_token_expiry_date;
    }

    public String getViewer_static_token() {
        return viewer_static_token;
    }

    public void setViewer_static_token(String viewer_static_token) {
        this.viewer_static_token = viewer_static_token;
    }

    public int getViewer_static_token_expiry_date() {
        return viewer_static_token_expiry_date;
    }

    public void setViewer_static_token_expiry_date(int viewer_static_token_expiry_date) {
        this.viewer_static_token_expiry_date = viewer_static_token_expiry_date;
    }
}
