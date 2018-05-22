package com.nutizen.nu.bean.request;

import java.io.Serializable;

public class ViewDetailReqBean implements Serializable {

    private String birthdate;
    private String address;
    private Integer gender;

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

}