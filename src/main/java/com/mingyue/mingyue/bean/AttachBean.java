package com.mingyue.mingyue.bean;

public class AttachBean extends BaseBean{

    private String path;
    private String name;
    private String uuid;
    private String type;

    private String attachSubType;
    private String attachType;

    public String getAttachSubType() {
        return attachSubType;
    }

    public void setAttachSubType(String attachSubType) {
        this.attachSubType = attachSubType;
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
