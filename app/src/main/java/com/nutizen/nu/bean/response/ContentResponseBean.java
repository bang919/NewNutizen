package com.nutizen.nu.bean.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContentResponseBean {

    /**
     * Search : [{"id":4313,"title":"Ibu - Gus Mus","chinese_title":null,"othesr_title":"","tagline":"","description":"Bagaiamana islam memandang seorang ibu","genres":"channels,-GUS MUS","datereleased":"2018-04-17 00:00:00","thumbnail":"https://vmse.nutizen.asia/api//uploads/thumbnail/20180417132000_default_img.jpg","rating":0,"type":"movie","directors":"","writers":"","cast":"","country":"","language":"","video_id":30158,"video_duration":59.8,"duration":59.8,"thumbnails":[],"subtitle":[],"poster":[],"trailer":[]}]
     * totalResults : 4
     * totalCount : 4
     * Response : True
     */

    private String totalResults;
    private String totalCount;
    private String Response;
    private ArrayList<SearchBean> Search;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ArrayList<SearchBean> getSearch() {
        return Search;
    }

    public void setSearch(ArrayList<SearchBean> Search) {
        this.Search = Search;
    }

    public static class SearchBean implements Serializable {

        private static final long serialVersionUID = 5642750732654L;

        /**
         * id : 4313
         * title : Ibu - Gus Mus
         * chinese_title : null
         * othesr_title :
         * tagline :
         * description : Bagaiamana islam memandang seorang ibu
         * genres : channels,-GUS MUS
         * datereleased : 2018-04-17 00:00:00
         * thumbnail : https://vmse.nutizen.asia/api//uploads/thumbnail/20180417132000_default_img.jpg
         * rating : 0
         * type : movie
         * directors :
         * writers :
         * cast :
         * country :
         * language :
         * video_id : 30158
         * video_duration : 59.8
         * duration : 59.8
         * thumbnails : []
         * subtitle : []
         * poster : []
         * trailer : []
         */

        private int id;
        private String title;
        private Object chinese_title;
        private String othesr_title;
        private String tagline;
        private String description;
        private String genres;
        private String datereleased;
        private String thumbnail;
        private int rating;
        private String type = "movie";
        private String directors;
        private String writers;
        private String cast;
        private String country;
        private String language;
        private int video_id;
        private double video_duration;
        private double duration;
        private List<String> thumbnails;
        private List<String> subtitle;
        private List<String> poster;
        private List<String> trailer;
        private String writter;//目前writers为空，Nu做法是在genres用"-"标志标记的writter，需手动解析

        public String getWritter() {
            return writter;
        }

        public void setWritter(String writter) {
            this.writter = writter;
        }

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

        public Object getChinese_title() {
            return chinese_title;
        }

        public void setChinese_title(Object chinese_title) {
            this.chinese_title = chinese_title;
        }

        public String getOthesr_title() {
            return othesr_title;
        }

        public void setOthesr_title(String othesr_title) {
            this.othesr_title = othesr_title;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGenres() {
            return genres;
        }

        public void setGenres(String genres) {
            this.genres = genres;
        }

        public String getDatereleased() {
            return datereleased;
        }

        public void setDatereleased(String datereleased) {
            this.datereleased = datereleased;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getType() {
            return type;
        }

        public String getDirectors() {
            return directors;
        }

        public void setDirectors(String directors) {
            this.directors = directors;
        }

        public String getWriters() {
            return writers;
        }

        public void setWriters(String writers) {
            this.writers = writers;
        }

        public String getCast() {
            return cast;
        }

        public void setCast(String cast) {
            this.cast = cast;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public double getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(double video_duration) {
            this.video_duration = video_duration;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public List<?> getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(List<String> thumbnails) {
            this.thumbnails = thumbnails;
        }

        public List<String> getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(List<String> subtitle) {
            this.subtitle = subtitle;
        }

        public List<String> getPoster() {
            return poster;
        }

        public void setPoster(List<String> poster) {
            this.poster = poster;
        }

        public List<String> getTrailer() {
            return trailer;
        }

        public void setTrailer(List<String> trailer) {
            this.trailer = trailer;
        }
    }
}
