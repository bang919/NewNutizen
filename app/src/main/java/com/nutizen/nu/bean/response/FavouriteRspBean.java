package com.nutizen.nu.bean.response;

import java.util.List;

public class FavouriteRspBean {

    /**
     * favor_id : 20110
     * viewer_content_type : contributor
     * viewer_favor_createdate : 2018-05-16T05:20:47.000Z
     * content_id : 48289
     * content_title : KULINER: Enaknya Pol
     * content_chinese_title : KULINER: Enaknya Pol
     * content_others_title : KULINER: Enaknya Pol
     * content_thumbnail : https://vmse.nutizen.asia/api/uploads/profile/20180421110246_img.jpg
     * content_poster : {"vertical":[{"poster_title":"","poster_url":"https://vmse.nutizen.asia/api/uploads/thumbnail/20180421110212_thumbnail_img.jpg","poster_created":"2018-04-21T04:02:12.000Z"}],"horizontal":[{"poster_title":"","poster_url":"https://vmse.nutizen.asia/api/uploads/thumbnail/20180421110212_thumbnail2_img.jpg","poster_created":"2018-04-21T04:02:13.000Z"}]}
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
        private List<VerticalBean> vertical;
        private List<HorizontalBean> horizontal;

        public List<VerticalBean> getVertical() {
            return vertical;
        }

        public void setVertical(List<VerticalBean> vertical) {
            this.vertical = vertical;
        }

        public List<HorizontalBean> getHorizontal() {
            return horizontal;
        }

        public void setHorizontal(List<HorizontalBean> horizontal) {
            this.horizontal = horizontal;
        }

        public static class VerticalBean {
            /**
             * poster_title :
             * poster_url : https://vmse.nutizen.asia/api/uploads/thumbnail/20180421110212_thumbnail_img.jpg
             * poster_created : 2018-04-21T04:02:12.000Z
             */

            private String poster_title;
            private String poster_url;
            private String poster_created;

            public String getPoster_title() {
                return poster_title;
            }

            public void setPoster_title(String poster_title) {
                this.poster_title = poster_title;
            }

            public String getPoster_url() {
                return poster_url;
            }

            public void setPoster_url(String poster_url) {
                this.poster_url = poster_url;
            }

            public String getPoster_created() {
                return poster_created;
            }

            public void setPoster_created(String poster_created) {
                this.poster_created = poster_created;
            }
        }

        public static class HorizontalBean {
            /**
             * poster_title :
             * poster_url : https://vmse.nutizen.asia/api/uploads/thumbnail/20180421110212_thumbnail2_img.jpg
             * poster_created : 2018-04-21T04:02:13.000Z
             */

            private String poster_title;
            private String poster_url;
            private String poster_created;

            public String getPoster_title() {
                return poster_title;
            }

            public void setPoster_title(String poster_title) {
                this.poster_title = poster_title;
            }

            public String getPoster_url() {
                return poster_url;
            }

            public void setPoster_url(String poster_url) {
                this.poster_url = poster_url;
            }

            public String getPoster_created() {
                return poster_created;
            }

            public void setPoster_created(String poster_created) {
                this.poster_created = poster_created;
            }
        }
    }
}
