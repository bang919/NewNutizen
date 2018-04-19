package com.nutizen.nu.bean.request;

/**
 * Created by Administrator on 2017/3/17.
 */

public class CommentBean {

    public CommentBean(String content_type, int content_id, String comment) {
        this.content_type = content_type;
        this.content_id = content_id;
        this.comment = comment;
    }

    /**
     * content_type : movie
     * content_id : 12
     * comment : hahah
     */

    private String content_type;
    private int content_id;
    private String comment;

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
}
