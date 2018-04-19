package com.nutizen.nu.bean.response;

import android.text.TextUtils;

public class CommentResult {


    /**
     * _id : 59ded2f86174a009cd230c3d
     * content_type : movie
     * content_id : 2983
     * comment : Hh
     * date : 2017-10-12T02:27:04.604Z
     * user : {"viewer_username":"bangtest","viewer_email":"bb501591443@163.com","viewer_firstname":"","viewer_lastname":"","viewer_mobile":"","viewer_address":"bigbang alex","viewer_postcode":"","viewer_country":"","viewer_birthdate":"1983-01-02","viewer_gender":0,"viewer_nickname":"","marital":null,"is_third":0,"viewer_thumbnail":"https://vmsenu.onwardsmg.net/api/uploads/profile/xhVe4ICAVA2C4EkwGXLDy0JL434jNmna.jpg","viewer_id":23882}
     */

    private String _id;
    private String content_type;
    private int content_id;
    private String comment;
    private String date;
    private UserBean user;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * viewer_username : bangtest
         * viewer_email : bb501591443@163.com
         * viewer_firstname :
         * viewer_lastname :
         * viewer_mobile :
         * viewer_address : bigbang alex
         * viewer_postcode :
         * viewer_country :
         * viewer_birthdate : 1983-01-02
         * viewer_gender : 0
         * viewer_nickname :
         * marital : null
         * is_third : 0
         * viewer_thumbnail : https://vmsenu.onwardsmg.net/api/uploads/profile/xhVe4ICAVA2C4EkwGXLDy0JL434jNmna.jpg
         * viewer_id : 23882
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
        private int viewer_gender;
        private String viewer_nickname;
        private Object marital;
        private int is_third;
        private String viewer_thumbnail;
        private int viewer_id;

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

        public int getViewer_gender() {
            return viewer_gender;
        }

        public void setViewer_gender(int viewer_gender) {
            this.viewer_gender = viewer_gender;
        }

        public String getViewer_nickname() {
            return viewer_nickname;
        }

        public void setViewer_nickname(String viewer_nickname) {
            this.viewer_nickname = viewer_nickname;
        }

        public Object getMarital() {
            return marital;
        }

        public void setMarital(Object marital) {
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

        public int getViewer_id() {
            return viewer_id;
        }

        public void setViewer_id(int viewer_id) {
            this.viewer_id = viewer_id;
        }

        public String getCommentNameShow(){
            String showName;
//            if (TextUtils.isEmpty(viewer_username) || (viewer_username.length() >= 15 && PhoneUtils.isNumeric(viewer_username))) {//第三方登录
            if (is_third == 1) {//第三方登录
                if(!TextUtils.isEmpty(viewer_email)){
                    showName = viewer_email.split("@")[0];
                }else {
                    showName = viewer_nickname;
                }
            }else {//不是第三方登录
                if (!TextUtils.isEmpty(viewer_username)){
                    showName = viewer_username.split("@")[0];
                }else if(!TextUtils.isEmpty(viewer_email)) {
                    showName = viewer_email.split("@")[0];
                }else {
                    showName = viewer_nickname;
                }
            }
            return showName;
        }
    }

}
