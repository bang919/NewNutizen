package com.nutizen.nu.bean.response;

import java.util.List;

public class FavouriteRspBean {
    /**
     * favor_id : 19594
     * viewer_content_type : movie
     * viewer_favor_createdate : 2018-04-21T10:35:53.000Z
     * content_id : 4323
     * content_title : KH. Abdul Wahab Chasbullah - Ulama Arif Peletak Dasar Ke Indonesiaan
     * content_chinese_title : null
     * content_others_title :
     * content_thumbnail : https://vmse.nutizen.asia/api/uploads/thumbnail/20180420155626_default_img.jpg
     * content_poster : {"vertical":[],"horizontal":[]}
     */

    private int favor_id;
    private String viewer_content_type;
    private String viewer_favor_createdate;
    private int content_id;
    private String content_title;
    private String content_chinese_title;
    private String content_others_title;
    private String content_thumbnail;
    private ContentPosterBean content_poster;

    public int getFavor_id() {
        return favor_id;
    }

    public void setFavor_id(int favor_id) {
        this.favor_id = favor_id;
    }

    public String getViewer_content_type() {
        return viewer_content_type;
    }

    public void setViewer_content_type(String viewer_content_type) {
        this.viewer_content_type = viewer_content_type;
    }

    public String getViewer_favor_createdate() {
        return viewer_favor_createdate;
    }

    public void setViewer_favor_createdate(String viewer_favor_createdate) {
        this.viewer_favor_createdate = viewer_favor_createdate;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public String getContent_chinese_title() {
        return content_chinese_title;
    }

    public void setContent_chinese_title(String content_chinese_title) {
        this.content_chinese_title = content_chinese_title;
    }

    public String getContent_others_title() {
        return content_others_title;
    }

    public void setContent_others_title(String content_others_title) {
        this.content_others_title = content_others_title;
    }

    public String getContent_thumbnail() {
        return content_thumbnail;
    }

    public void setContent_thumbnail(String content_thumbnail) {
        this.content_thumbnail = content_thumbnail;
    }

    public ContentPosterBean getContent_poster() {
        return content_poster;
    }

    public void setContent_poster(ContentPosterBean content_poster) {
        this.content_poster = content_poster;
    }

    public static class ContentPosterBean {
        private List<?> vertical;
        private List<?> horizontal;

        public List<?> getVertical() {
            return vertical;
        }

        public void setVertical(List<?> vertical) {
            this.vertical = vertical;
        }

        public List<?> getHorizontal() {
            return horizontal;
        }

        public void setHorizontal(List<?> horizontal) {
            this.horizontal = horizontal;
        }
    }
}
