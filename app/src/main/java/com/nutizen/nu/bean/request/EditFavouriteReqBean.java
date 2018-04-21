package com.nutizen.nu.bean.request;

public class EditFavouriteReqBean {

    public static final String EDIT_MARK = "mark";
    public static final String EDIT_UNMARK = "unmark";

    /**
     * viewerid : 23886
     * operation : mark
     * contenttype : movie
     * contentid : 4323
     */

    private int viewerid;
    private String operation;
    private String contenttype;
    private int contentid;

    public int getViewerid() {
        return viewerid;
    }

    public void setViewerid(int viewerid) {
        this.viewerid = viewerid;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public int getContentid() {
        return contentid;
    }

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }
}
