package com.nutizen.nu.bean.response;

/**
 * Created by bigbang on 2018/4/11.
 */

public class LiveResponseBean {

    /**
     * id : 244
     * title : Elshinta FM
     * recommend : 0
     * category : radio
     * synopsis :
     * thumbnail : https://vmse.nutizen.asia/api/uploads/thumbnail/20170411190055_default_img.png
     * status : 1
     * delay : 0
     * type : live
     * epgUrl :
     * url : http://202.137.4.147:8000/
     * uuid : zBraxC7heLc9
     * live_type : 2
     */

    private int id;
    private String title;
    private int recommend;
    private String category;
    private String synopsis;
    private String thumbnail;
    private int status;
    private int delay;
    private String type;
    private String epgUrl;
    private String url;
    private String uuid;
    private int live_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEpgUrl() {
        return epgUrl;
    }

    public void setEpgUrl(String epgUrl) {
        this.epgUrl = epgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLive_type() {
        return live_type;
    }

    public void setLive_type(int live_type) {
        this.live_type = live_type;
    }
}
