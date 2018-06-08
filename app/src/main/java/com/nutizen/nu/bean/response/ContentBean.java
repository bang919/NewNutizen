package com.nutizen.nu.bean.response;

import java.io.Serializable;

/**
 * 用来记录
 */
public class ContentBean  implements Serializable {

    public static final String CONTENT_BEAN = "content bean";
    private static final long serialVersionUID = 468465135131616L;

    private String type;
    private ContentResponseBean.SearchBean movieBean;
    private LiveResponseBean liveBean;
    private String createTimeS;//"yyyy-MM-dd HH:mm:ss"
    private long createTimeL;//long来表示

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContentResponseBean.SearchBean getMovieBean() {
        return movieBean;
    }

    public void setMovieBean(ContentResponseBean.SearchBean movieBean) {
        this.movieBean = movieBean;
    }

    public LiveResponseBean getLiveBean() {
        return liveBean;
    }

    public void setLiveBean(LiveResponseBean liveBean) {
        this.liveBean = liveBean;
    }

    public String getCreateTimeS() {
        return createTimeS;
    }

    public void setCreateTimeS(String createTimeS) {
        this.createTimeS = createTimeS;
    }

    public long getCreateTimeL() {
        return createTimeL;
    }

    public void setCreateTimeL(long createTimeL) {
        this.createTimeL = createTimeL;
    }

    public int getId() {
        switch (type) {
            case "movie":
                return movieBean.getId();
            case "live":
                return liveBean.getId();
            default:
                return 0;
        }
    }

    public String getTitle() {
        switch (type) {
            case "movie":
                return movieBean.getTitle();
            case "live":
                return liveBean.getTitle();
            default:
                return null;
        }
    }

    public String getThumbnail() {
        switch (type) {
            case "movie":
                return movieBean.getThumbnail();
            case "live":
                return liveBean.getThumbnail();
            default:
                return null;
        }
    }
}
