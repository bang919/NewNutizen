package com.nutizen.nu.bean.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/6.
 */

public class LoginResponseBean implements Serializable {
    private static final long serialVersionUID = 1523148961L;


    /**
     * status : true
     * viewer_id : 23886
     * viewer_token : dac8292361d8af9e6a0f0ffb7ecd95c00ed37798c6a40b96bb36495a3bc680ef
     * viewer_token_expiry_date : 1524214031
     * viewer_static_token : 56d111ac0dfa3b2a8ab2a9818baeed319126ebc6533e69ddbc878b141b52cedf
     * is_contributor : false
     * detail : {"viewer_username":"bangtest","viewer_email":"bangtest@163.com","viewer_firstname":null,"viewer_lastname":null,"viewer_mobile":null,"viewer_address":null,"viewer_postcode":null,"viewer_country":null,"viewer_birthdate":null,"viewer_gender":null,"viewer_nickname":null,"marital":null,"is_third":0,"viewer_thumbnail":null,"viewer_vip":0,"gender":2}
     */

    private boolean status;
    private int viewer_id;
    private String viewer_token;
    private int viewer_token_expiry_date;
    private String viewer_static_token;
    private boolean is_contributor;
    private DetailBean detail;

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

    public boolean isIs_contributor() {
        return is_contributor;
    }

    public void setIs_contributor(boolean is_contributor) {
        this.is_contributor = is_contributor;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public static class DetailBean implements Serializable {
        private static final long serialVersionUID = 15327981L;
        /**
         * viewer_username : bangtest
         * viewer_email : bangtest@163.com
         * viewer_firstname : null
         * viewer_lastname : null
         * viewer_mobile : null
         * viewer_address : null
         * viewer_postcode : null
         * viewer_country : null
         * viewer_birthdate : null
         * viewer_gender : null
         * viewer_nickname : null
         * marital : null
         * is_third : 0
         * viewer_thumbnail : null
         * viewer_vip : 0
         * gender : 2
         */

        private String viewer_username;
        private String viewer_email;
        private String viewer_firstname;
        private String viewer_lastname;
        private String viewer_mobile;
        private String viewer_address;
        private String viewer_postcode;
        private String viewer_country;
        private String viewer_birthdate;
        private String viewer_gender;
        private String viewer_nickname;
        private String marital;
        private int is_third;
        private String viewer_thumbnail;
        private int viewer_vip;
        private int gender;

        public String getViewer_username() {
            return viewer_username;
        }

        public void setViewer_username(String viewer_username) {
            this.viewer_username = viewer_username;
        }

        public String getViewer_email() {
            return viewer_email;
        }

        public void setViewer_email(String viewer_email) {
            this.viewer_email = viewer_email;
        }

        public String getViewer_firstname() {
            return viewer_firstname;
        }

        public void setViewer_firstname(String viewer_firstname) {
            this.viewer_firstname = viewer_firstname;
        }

        public String getViewer_lastname() {
            return viewer_lastname;
        }

        public void setViewer_lastname(String viewer_lastname) {
            this.viewer_lastname = viewer_lastname;
        }

        public String getViewer_mobile() {
            return viewer_mobile;
        }

        public void setViewer_mobile(String viewer_mobile) {
            this.viewer_mobile = viewer_mobile;
        }

        public String getViewer_address() {
            return viewer_address;
        }

        public void setViewer_address(String viewer_address) {
            this.viewer_address = viewer_address;
        }

        public String getViewer_postcode() {
            return viewer_postcode;
        }

        public void setViewer_postcode(String viewer_postcode) {
            this.viewer_postcode = viewer_postcode;
        }

        public String getViewer_country() {
            return viewer_country;
        }

        public void setViewer_country(String viewer_country) {
            this.viewer_country = viewer_country;
        }

        public String getViewer_birthdate() {
            return viewer_birthdate;
        }

        public void setViewer_birthdate(String viewer_birthdate) {
            this.viewer_birthdate = viewer_birthdate;
        }

        public String getViewer_gender() {
            return viewer_gender;
        }

        public void setViewer_gender(String viewer_gender) {
            this.viewer_gender = viewer_gender;
        }

        public String getViewer_nickname() {
            return viewer_nickname;
        }

        public void setViewer_nickname(String viewer_nickname) {
            this.viewer_nickname = viewer_nickname;
        }

        public String getMarital() {
            return marital;
        }

        public void setMarital(String marital) {
            this.marital = marital;
        }

        public int getIs_third() {
            return is_third;
        }

        public void setIs_third(int is_third) {
            this.is_third = is_third;
        }

        public String getViewer_thumbnail() {
            return viewer_thumbnail;
        }

        public void setViewer_thumbnail(String viewer_thumbnail) {
            this.viewer_thumbnail = viewer_thumbnail;
        }

        public int getViewer_vip() {
            return viewer_vip;
        }

        public void setViewer_vip(int viewer_vip) {
            this.viewer_vip = viewer_vip;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
    }
}
