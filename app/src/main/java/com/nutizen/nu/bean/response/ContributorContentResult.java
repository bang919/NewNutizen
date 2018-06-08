package com.nutizen.nu.bean.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContributorContentResult implements Serializable {


    /**
     * Search : [{"movie_id":60,"movie_title":"movdon","movie_chinese_title":null,"movie_language":"","movie_dubbing_language":"","movie_country":"","movie_others_title":"","movie_tagline":"","movie_description":"","movie_rating":0,"movie_restriction":"","movie_award":"","movie_genres":"Action","movie_datereleased":"0000-00-00 00:00:00","movie_directors":"","movie_writers":"","movie_cast":"","movie_datecreated":"2016-12-15T09:50:38.000Z","movie_delflag":0,"movie_status":0,"video_id":403,"movie_thumbnail":"","movie_recommend":0,"account_id":72,"movie_duration":0},{"movie_id":51,"movie_title":"超清2","movie_chinese_title":null,"movie_language":"","movie_dubbing_language":"","movie_country":"","movie_others_title":"999","movie_tagline":"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111","movie_description":"","movie_rating":0,"movie_restriction":"PG","movie_award":"","movie_genres":"Action,History,Music,爱好2,aaaaa","movie_datereleased":"2016-11-07T16:00:00.000Z","movie_directors":"","movie_writers":"","movie_cast":"","movie_datecreated":"2016-11-08T07:40:55.000Z","movie_delflag":0,"movie_status":1,"video_id":1894,"movie_thumbnail":"20161108154055_default_img.jpg","movie_recommend":0,"account_id":72,"movie_duration":125,"thumbnail":"http://172.16.1.11/api/uploads/thumbnail/20161108154055_default_img.jpg"},{"movie_id":50,"movie_title":"测试缩略图丢失","movie_chinese_title":null,"movie_language":"","movie_dubbing_language":"","movie_country":"","movie_others_title":"","movie_tagline":"test","movie_description":"test。。。。2016.11.3新建，上传的缩略图名称是\u201c竖屏.png\u201d,,,http://seafront.chaotv.asia:8808/api/uploads/thumbnail/20161103173331_default_img.png","movie_rating":1,"movie_restriction":"","movie_award":"","movie_genres":"Action","movie_datereleased":"2016-11-02T16:00:00.000Z","movie_directors":"","movie_writers":"","movie_cast":"","movie_datecreated":"2016-11-03T09:33:31.000Z","movie_delflag":0,"movie_status":1,"video_id":1802,"movie_thumbnail":"20161103173331_default_img.png","movie_recommend":0,"account_id":72,"movie_duration":91.36,"thumbnail":"http://172.16.1.11/api/uploads/thumbnail/20161103173331_default_img.png"}]
     * totalResults : 3
     * totalCount : 3
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
         * movie_id : 60
         * movie_title : movdon
         * movie_chinese_title : null
         * movie_language :
         * movie_dubbing_language :
         * movie_country :
         * movie_others_title :
         * movie_tagline :
         * movie_description :
         * movie_rating : 0
         * movie_restriction :
         * movie_award :
         * movie_genres : Action
         * movie_datereleased : 0000-00-00 00:00:00
         * movie_directors :
         * movie_writers :
         * movie_cast :
         * movie_datecreated : 2016-12-15T09:50:38.000Z
         * movie_delflag : 0
         * movie_status : 0
         * video_id : 403
         * movie_thumbnail :
         * movie_recommend : 0
         * account_id : 72
         * movie_duration : 0
         * thumbnail : http://172.16.1.11/api/uploads/thumbnail/20161108154055_default_img.jpg
         */

        private int movie_id;
        private String movie_title;
        private Object movie_chinese_title;
        private String movie_language;
        private String movie_dubbing_language;
        private String movie_country;
        private String movie_others_title;
        private String movie_tagline;
        private String movie_description;
        private int movie_rating;
        private String movie_restriction;
        private String movie_award;
        private String movie_genres;
        private String movie_datereleased;
        private String movie_directors;
        private String movie_writers;
        private String movie_cast;
        private String movie_datecreated;
        private int movie_delflag;
        private int movie_status;
        private int video_id;
        private String movie_thumbnail;
        private int movie_recommend;
        private int account_id;
        private double movie_duration;
        private String thumbnail;

        public int getMovie_id() {
            return movie_id;
        }

        public void setMovie_id(int movie_id) {
            this.movie_id = movie_id;
        }

        public String getMovie_title() {
            return movie_title;
        }

        public void setMovie_title(String movie_title) {
            this.movie_title = movie_title;
        }

        public Object getMovie_chinese_title() {
            return movie_chinese_title;
        }

        public void setMovie_chinese_title(Object movie_chinese_title) {
            this.movie_chinese_title = movie_chinese_title;
        }

        public String getMovie_language() {
            return movie_language;
        }

        public void setMovie_language(String movie_language) {
            this.movie_language = movie_language;
        }

        public String getMovie_dubbing_language() {
            return movie_dubbing_language;
        }

        public void setMovie_dubbing_language(String movie_dubbing_language) {
            this.movie_dubbing_language = movie_dubbing_language;
        }

        public String getMovie_country() {
            return movie_country;
        }

        public void setMovie_country(String movie_country) {
            this.movie_country = movie_country;
        }

        public String getMovie_others_title() {
            return movie_others_title;
        }

        public void setMovie_others_title(String movie_others_title) {
            this.movie_others_title = movie_others_title;
        }

        public String getMovie_tagline() {
            return movie_tagline;
        }

        public void setMovie_tagline(String movie_tagline) {
            this.movie_tagline = movie_tagline;
        }

        public String getMovie_description() {
            return movie_description;
        }

        public void setMovie_description(String movie_description) {
            this.movie_description = movie_description;
        }

        public int getMovie_rating() {
            return movie_rating;
        }

        public void setMovie_rating(int movie_rating) {
            this.movie_rating = movie_rating;
        }

        public String getMovie_restriction() {
            return movie_restriction;
        }

        public void setMovie_restriction(String movie_restriction) {
            this.movie_restriction = movie_restriction;
        }

        public String getMovie_award() {
            return movie_award;
        }

        public void setMovie_award(String movie_award) {
            this.movie_award = movie_award;
        }

        public String getMovie_genres() {
            return movie_genres;
        }

        public void setMovie_genres(String movie_genres) {
            this.movie_genres = movie_genres;
        }

        public String getMovie_datereleased() {
            return movie_datereleased;
        }

        public void setMovie_datereleased(String movie_datereleased) {
            this.movie_datereleased = movie_datereleased;
        }

        public String getMovie_directors() {
            return movie_directors;
        }

        public void setMovie_directors(String movie_directors) {
            this.movie_directors = movie_directors;
        }

        public String getMovie_writers() {
            return movie_writers;
        }

        public void setMovie_writers(String movie_writers) {
            this.movie_writers = movie_writers;
        }

        public String getMovie_cast() {
            return movie_cast;
        }

        public void setMovie_cast(String movie_cast) {
            this.movie_cast = movie_cast;
        }

        public String getMovie_datecreated() {
            return movie_datecreated;
        }

        public void setMovie_datecreated(String movie_datecreated) {
            this.movie_datecreated = movie_datecreated;
        }

        public int getMovie_delflag() {
            return movie_delflag;
        }

        public void setMovie_delflag(int movie_delflag) {
            this.movie_delflag = movie_delflag;
        }

        public int getMovie_status() {
            return movie_status;
        }

        public void setMovie_status(int movie_status) {
            this.movie_status = movie_status;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public String getMovie_thumbnail() {
            return movie_thumbnail;
        }

        public void setMovie_thumbnail(String movie_thumbnail) {
            this.movie_thumbnail = movie_thumbnail;
        }

        public int getMovie_recommend() {
            return movie_recommend;
        }

        public void setMovie_recommend(int movie_recommend) {
            this.movie_recommend = movie_recommend;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public double getMovie_duration() {
            return movie_duration;
        }

        public void setMovie_duration(double movie_duration) {
            this.movie_duration = movie_duration;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }

    public ContentResponseBean toContentResponseBean() {
        ContentResponseBean contentResponseBean = new ContentResponseBean();
        contentResponseBean.setTotalResults(getTotalResults());
        contentResponseBean.setTotalCount(getTotalCount());
        contentResponseBean.setResponse("True");
        ArrayList<ContentResponseBean.SearchBean> searchBeans = new ArrayList<>();
        for (ContributorContentResult.SearchBean result : getSearch()) {
            ContentResponseBean.SearchBean searchBean = new ContentResponseBean.SearchBean();
            searchBean.setId(result.getMovie_id());
            searchBean.setTitle(result.getMovie_title());
            //不设置太多了，影响效率啊。。。。
//                            searchBean.setChinese_title(result.getMovie_chinese_title());
//                            searchBean.setOthesr_title(result.getMovie_others_title());
            searchBean.setTagline(result.getMovie_tagline());
            searchBean.setDescription(result.getMovie_description());
            searchBean.setGenres(result.getMovie_genres());
            searchBean.setDatereleased(result.getMovie_datereleased());
            searchBean.setThumbnail(result.getMovie_thumbnail());
            searchBean.setRating(result.getMovie_rating());
//                            searchBean.setDirectors(result.getMovie_directors());
//                            searchBean.setWriters(result.getMovie_writers());
//                            searchBean.setCast(result.getMovie_cast());
            searchBean.setCountry(result.getMovie_country());
            searchBean.setLanguage(result.getMovie_language());
            searchBean.setVideo_id(result.getVideo_id());
            searchBean.setDuration(result.getMovie_duration());
            searchBeans.add(searchBean);
        }
        contentResponseBean.setSearch(searchBeans);
        return contentResponseBean;
    }
}