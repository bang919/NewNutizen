package com.nutizen.nu.bean.response;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ContributorLiveResult {

    /**
     * Search : [{"live_id":241,"live_source_type":4,"live_title":"bang1","live_description":"bang3","live_genres":"bang2","live_country":null,"live_epg_url":null,"live_recommend":1,"plan_id":null,"live_geolock":null,"live_delflag":0,"live_status":1,"live_datecreated":"2017-04-12T08:56:53.000Z","live_thumbnail":null,"live_tagline":null,"live_source":"rtmp://172.16.1.33:44435/test/SNkLpkPxnl9v.sdp","live_delay":0,"live_autoencode":1,"live_uuid":"SNkLpkPxnl9v","account_id":72,"rtmpUrl":"rtmp://172.16.1.33:44435/seafront/SNkLpkPxnl9v.sdp.sdp","rtspUrl":"rtsp://172.16.1.33:44434/seafront/SNkLpkPxnl9v.sdp.sdp","httpUrl":"http://172.16.1.33:8088/live/SNkLpkPxnl9v.sdp/default/0/hls/0/index.m3u8"},{"live_id":240,"live_source_type":4,"live_title":"test11","live_description":"test11","live_genres":"test11","live_country":null,"live_epg_url":null,"live_recommend":1,"plan_id":null,"live_geolock":null,"live_delflag":0,"live_status":1,"live_datecreated":"2017-04-12T08:54:35.000Z","live_thumbnail":null,"live_tagline":null,"live_source":"rtmp://172.16.1.33:44435/test/VmDcldJ4LkZV.sdp","live_delay":0,"live_autoencode":1,"live_uuid":"VmDcldJ4LkZV","account_id":72,"rtmpUrl":"rtmp://172.16.1.33:44435/seafront/VmDcldJ4LkZV.sdp.sdp","rtspUrl":"rtsp://172.16.1.33:44434/seafront/VmDcldJ4LkZV.sdp.sdp","httpUrl":"http://172.16.1.33:8088/live/VmDcldJ4LkZV.sdp/default/0/hls/0/index.m3u8"}]
     * totalResults : 2
     * totalCount : 2
     * Response : true
     */

    private int totalResults;
    private int totalCount;
    private boolean Response;
    private List<SearchBean> Search;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isResponse() {
        return Response;
    }

    public void setResponse(boolean Response) {
        this.Response = Response;
    }

    public List<SearchBean> getSearch() {
        return Search;
    }

    public void setSearch(List<SearchBean> Search) {
        this.Search = Search;
    }

    public static class SearchBean {
        /**
         * live_id : 241
         * live_source_type : 4
         * live_title : bang1
         * live_description : bang3
         * live_genres : bang2
         * live_country : null
         * live_epg_url : null
         * live_recommend : 1
         * plan_id : null
         * live_geolock : null
         * live_delflag : 0
         * live_status : 1
         * live_datecreated : 2017-04-12T08:56:53.000Z
         * live_thumbnail : null
         * live_tagline : null
         * live_source : rtmp://172.16.1.33:44435/test/SNkLpkPxnl9v.sdp
         * live_delay : 0
         * live_autoencode : 1
         * live_uuid : SNkLpkPxnl9v
         * account_id : 72
         * rtmpUrl : rtmp://172.16.1.33:44435/seafront/SNkLpkPxnl9v.sdp.sdp
         * rtspUrl : rtsp://172.16.1.33:44434/seafront/SNkLpkPxnl9v.sdp.sdp
         * httpUrl : http://172.16.1.33:8088/live/SNkLpkPxnl9v.sdp/default/0/hls/0/index.m3u8
         */

        private int live_id;
        private int live_source_type;
        private String live_title;
        private String live_description;
        private String live_genres;
        private String live_country;
        private String live_epg_url;
        private int live_recommend;
        private String plan_id;
        private String live_geolock;
        private int live_delflag;
        private int live_status;
        private String live_datecreated;
        private String live_thumbnail;
        private String live_tagline;
        private String live_source;
        private int live_delay;
        private int live_autoencode;
        private String live_uuid;
        private int account_id;
        private String rtmpUrl;
        private String rtspUrl;
        private String httpUrl;

        public int getLive_id() {
            return live_id;
        }

        public void setLive_id(int live_id) {
            this.live_id = live_id;
        }

        public int getLive_source_type() {
            return live_source_type;
        }

        public void setLive_source_type(int live_source_type) {
            this.live_source_type = live_source_type;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getLive_description() {
            return live_description;
        }

        public void setLive_description(String live_description) {
            this.live_description = live_description;
        }

        public String getLive_genres() {
            return live_genres;
        }

        public void setLive_genres(String live_genres) {
            this.live_genres = live_genres;
        }

        public String getLive_country() {
            return live_country;
        }

        public void setLive_country(String live_country) {
            this.live_country = live_country;
        }

        public String getLive_epg_url() {
            return live_epg_url;
        }

        public void setLive_epg_url(String live_epg_url) {
            this.live_epg_url = live_epg_url;
        }

        public int getLive_recommend() {
            return live_recommend;
        }

        public void setLive_recommend(int live_recommend) {
            this.live_recommend = live_recommend;
        }

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getLive_geolock() {
            return live_geolock;
        }

        public void setLive_geolock(String live_geolock) {
            this.live_geolock = live_geolock;
        }

        public int getLive_delflag() {
            return live_delflag;
        }

        public void setLive_delflag(int live_delflag) {
            this.live_delflag = live_delflag;
        }

        public int getLive_status() {
            return live_status;
        }

        public void setLive_status(int live_status) {
            this.live_status = live_status;
        }

        public String getLive_datecreated() {
            return live_datecreated;
        }

        public void setLive_datecreated(String live_datecreated) {
            this.live_datecreated = live_datecreated;
        }

        public String getLive_thumbnail() {
            return live_thumbnail;
        }

        public void setLive_thumbnail(String live_thumbnail) {
            this.live_thumbnail = live_thumbnail;
        }

        public String getLive_tagline() {
            return live_tagline;
        }

        public void setLive_tagline(String live_tagline) {
            this.live_tagline = live_tagline;
        }

        public String getLive_source() {
            return live_source;
        }

        public void setLive_source(String live_source) {
            this.live_source = live_source;
        }

        public int getLive_delay() {
            return live_delay;
        }

        public void setLive_delay(int live_delay) {
            this.live_delay = live_delay;
        }

        public int getLive_autoencode() {
            return live_autoencode;
        }

        public void setLive_autoencode(int live_autoencode) {
            this.live_autoencode = live_autoencode;
        }

        public String getLive_uuid() {
            return live_uuid;
        }

        public void setLive_uuid(String live_uuid) {
            this.live_uuid = live_uuid;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public String getRtmpUrl() {
            return rtmpUrl;
        }

        public void setRtmpUrl(String rtmpUrl) {
            this.rtmpUrl = rtmpUrl;
        }

        public String getRtspUrl() {
            return rtspUrl;
        }

        public void setRtspUrl(String rtspUrl) {
            this.rtspUrl = rtspUrl;
        }

        public String getHttpUrl() {
            return httpUrl;
        }

        public void setHttpUrl(String httpUrl) {
            this.httpUrl = httpUrl;
        }
    }
}
