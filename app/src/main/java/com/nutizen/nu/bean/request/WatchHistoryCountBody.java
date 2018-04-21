package com.nutizen.nu.bean.request;

public class WatchHistoryCountBody {

    /**
     * content_name : 3个分辨率，默认使用320播放
     * content_type : movie
     * content_id : 34
     * video_id : 2750
     * start_time : 2017-03-19 12:12:12
     * end_time : 2017-03-19 13:12:12
     */

    private String content_name;
    private String content_type;
    private int content_id;
    private int video_id;
    private String start_time;
    private String end_time;

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
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

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}