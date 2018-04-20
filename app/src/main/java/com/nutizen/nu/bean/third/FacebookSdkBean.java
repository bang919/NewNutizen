package com.nutizen.nu.bean.third;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/11/8.
 */

public class FacebookSdkBean {

    public FacebookSdkBean(HashMap<String, Object> hashMap,String token) {
        third_party_id = (String) hashMap.get("third_party_id");
        id = (int) hashMap.get("id");
        picture = (String)((HashMap)((HashMap)hashMap.get("picture")).get("data")).get("url");
        name = (String) hashMap.get("name");
        last_name = (String) hashMap.get("last_name");
        first_name = (String) hashMap.get("first_name");
        email = (String) hashMap.get("email");
        gender = (String) hashMap.get("gender");
        locale = (String) hashMap.get("locale");
        verified = (boolean) hashMap.get("verified");
        platform = "Facebook";
        this.token = token;
    }

    /**
     * third_party_id : Z6Uswm8V33Dvn3omRCDgBH7cUIU
     * id : 377749495979182
     * picture : {"data":{"is_silhouette":false,"url":"https://fb-s-a-a.akamaihd.net/h-ak-fbx/v/t1.0-1/c0.19.50.50/p50x50/13417539_128213280932806_7047807779500852487_n.jpg?oh=2ff586cfe938676b7330fbef40ab6dd8&oe=5A625721&__gda__=1516389450_a4a295581670b01e94f028d47d8ff5dc"}}
     * name : Onwards Dev
     * last_name : Dev
     * first_name : Onwards
     * email : fgyean@onwardsmg.com
     * gender : male
     * locale : en_GB
     * verified : true
     */

    private String third_party_id;
    private int id;
    private String picture;
    private String name;
    private String last_name;
    private String first_name;
    private String email;
    private String gender;
    private String locale;
    private boolean verified;

    public String getThird_party_id() {
        return third_party_id;
    }

    public void setThird_party_id(String third_party_id) {
        this.third_party_id = third_party_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }



    /**
     * platform : weixin
     * token : SBnhUeojiz
     * openid : oTeVBwPTaP9x6lDlJlpI--Y0eKHw
     */

    private String platform;
    private String token;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
