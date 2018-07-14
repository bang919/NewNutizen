package com.nutizen.nu.bean.request;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ArchiveBody {

    public ArchiveBody(String uuid, String title) {
        this.uuid = uuid;
        this.filename = title;
        this.path = uuid + ".sdp";
        this.name = title;
        this.action = 1;
    }

    /**
     * path :  aSZjmScuwEKK.sdp
     * name : test1
     * action : 1
     * uuid : aSZjmScuwEKK
     * filename : testArchive-1494308352
     */

    private String path;
    private String name;
    private int action;
    private String uuid;
    private String filename;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
