package com.nutizen.nu.bean.request;

/**
 * Created by Administrator on 2017/4/14.
 */

public class LiveStreamingBean {

    /**
     * title : test11
     * recommend : 1
     * category : test11
     * synopsis : test11
     * source_type : 4
     * type : RTSP
     */

    private String title;
    private String category;
    private String synopsis;
    private String type;

    public LiveStreamingBean(String title, String category, String synopsis, String type) {
        this.title = title;
        this.category = category;
        this.synopsis = synopsis;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
