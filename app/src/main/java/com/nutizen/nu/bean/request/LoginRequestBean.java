package com.nutizen.nu.bean.request;

/**
 * Created by Administrator on 2018/3/6.
 */

public class LoginRequestBean {

    /**
     * username_email : bigbang
     * password : 123456
     */

    private String username_email;
    private String password;

    public LoginRequestBean() {
    }

    public LoginRequestBean(String username_email, String password) {
        this.username_email = username_email;
        this.password = password;
    }

    public String getUsername_email() {
        return username_email;
    }

    public void setUsername_email(String username_email) {
        this.username_email = username_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
