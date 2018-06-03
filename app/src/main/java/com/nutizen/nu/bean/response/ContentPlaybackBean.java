package com.nutizen.nu.bean.response;

import java.util.ArrayList;

public class ContentPlaybackBean {


    /**
     * video_id : 30158
     * uuid : hg1b9BgVjKcN
     * video_filename : Gus Mus - Ibu.mp4
     * filename : hg1b9BgVjKcN.mp4
     * video_dateupload : 2018-04-17T04:42:57.000Z
     * video_duration : 59.8
     * video_encode_status : 5
     * video_used : 1
     * video_chapter_nums : 0
     * video_profile : [{"id":17350,"video_id":30158,"profile_name":"720p","url_http":"http://ocdn.nutizen.asia:44436/vod/hg1b9BgVjKcN/default/hls/0/hg1b9BgVjKcN_720p.m3u8","url_rtsp":"","encode_status":5,"profile_delflag":0,"last_update":"2018-04-17T04:43:32.000Z"}]
     */

    private int video_id;
    private String uuid;
    private String video_filename;
    private String filename;
    private String video_dateupload;
    private double video_duration;
    private String video_encode_status;
    private int video_used;
    private int video_chapter_nums;
    private ArrayList<VideoProfileBean> video_profile;

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getUuid() {
        return uuid;
    }

    //下载文件的名字
    public String getDownloadFileName(){
        return getUuid().concat(String.valueOf(getVideo_id()));
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVideo_filename() {
        return video_filename;
    }

    public void setVideo_filename(String video_filename) {
        this.video_filename = video_filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getVideo_dateupload() {
        return video_dateupload;
    }

    public void setVideo_dateupload(String video_dateupload) {
        this.video_dateupload = video_dateupload;
    }

    public double getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(double video_duration) {
        this.video_duration = video_duration;
    }

    public String getVideo_encode_status() {
        return video_encode_status;
    }

    public void setVideo_encode_status(String video_encode_status) {
        this.video_encode_status = video_encode_status;
    }

    public int getVideo_used() {
        return video_used;
    }

    public void setVideo_used(int video_used) {
        this.video_used = video_used;
    }

    public int getVideo_chapter_nums() {
        return video_chapter_nums;
    }

    public void setVideo_chapter_nums(int video_chapter_nums) {
        this.video_chapter_nums = video_chapter_nums;
    }

    public ArrayList<VideoProfileBean> getVideo_profile() {
        return video_profile;
    }

    public void setVideo_profile(ArrayList<VideoProfileBean> video_profile) {
        this.video_profile = video_profile;
    }

    public static class VideoProfileBean {
        /**
         * id : 17350
         * video_id : 30158
         * profile_name : 720p
         * url_http : http://ocdn.nutizen.asia:44436/vod/hg1b9BgVjKcN/default/hls/0/hg1b9BgVjKcN_720p.m3u8
         * url_rtsp :
         * encode_status : 5
         * profile_delflag : 0
         * last_update : 2018-04-17T04:43:32.000Z
         */

        private int id;
        private int video_id;
        private String profile_name;
        private String url_http;
        private String url_rtsp;
        private int encode_status;
        private int profile_delflag;
        private String last_update;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public String getProfile_name() {
            return profile_name;
        }

        public void setProfile_name(String profile_name) {
            this.profile_name = profile_name;
        }

        public String getUrl_http() {
            return url_http;
        }

        public void setUrl_http(String url_http) {
            this.url_http = url_http;
        }

        public String getUrl_rtsp() {
            return url_rtsp;
        }

        public void setUrl_rtsp(String url_rtsp) {
            this.url_rtsp = url_rtsp;
        }

        public int getEncode_status() {
            return encode_status;
        }

        public void setEncode_status(int encode_status) {
            this.encode_status = encode_status;
        }

        public int getProfile_delflag() {
            return profile_delflag;
        }

        public void setProfile_delflag(int profile_delflag) {
            this.profile_delflag = profile_delflag;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }
    }
}
