package com.nutizen.nu.bean.response;

import java.io.Serializable;

public class RegisterResponse implements Serializable {


    /**
     * status : true
     * viewer_id : 304
     * token : eafc94b7bf0b4f9c47328a1c1155c2936b19a667618ff0dba3c4aa3d54f5a59f
     * expiryDate : 1484727677
     * staticToken : b2136620984816d56a70b31c9e2506ef83bd618fd5e486c606d33fac5753c09a
     */

    public Boolean status;
    public Integer viewer_id;
    public String token;
    public Integer expiryDate;
    public String staticToken;

    /**
     * code : 400
     * message : Username or Email exists
     */

    public ErrBean err;

    public static class ErrBean {
        public int code;
        public String message;
    }
}
