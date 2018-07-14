package com.nutizen.nu.bean.response;

/**
 * Created by Administrator on 2017/4/14.
 */

public class LiveStreamingResult {

    /**
     * url : rtsp://172.16.1.33:554/test/9XAu8pxbNSNO.sdp
     * id : 243
     * uuid : 9XAu8pxbNSNO
     */

    private String url;
    private int id;
    private String uuid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
