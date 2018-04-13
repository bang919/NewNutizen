package com.nutizen.nu.bean.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class LoginResponseBean implements Serializable {
    private static final long serialVersionUID = 11L;

    /**
     * _id : 5a9d1753f1f30b3e18bb3dde
     * username : bigbang
     * nickname :
     * email : 501591443@qq.com
     * language : null
     * role : []
     * thumbnail : {"default":null}
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1YTlkMTc1M2YxZjMwYjNlMThiYjNkZGUiLCJhdWQiOiJQYW5lbCIsImlzcyI6IlBhbmVsIiwiaWF0IjoxNTIwMzA5NTYzLCJleHAiOjE1NTE4NjcxNjN9.To7-XXcW70vB4W_IAO82ic-mCH9xz96BfJiwAECAsAg
     */

    private String _id;
    private String username;
    private String nickname;
    private String email;
    private String language;
    private ThumbnailBean thumbnail;
    private String token;
    private List<String> role;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ThumbnailBean getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailBean thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public static class ThumbnailBean implements Serializable {
        private static final long serialVersionUID = 12L;
        /**
         * default : null
         */

        @SerializedName("default")
        private String defaultX;

        public String getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(String defaultX) {
            this.defaultX = defaultX;
        }
    }
}
