package com.mingyue.attach.bean;

import com.mingyue.base.bean.BaseBean;

public class Attach extends BaseBean {

    private String path;
    private String name;
    private String uuid;
    private String type;

    private String attachSubType;
    private String attachType;

    private Integer permissions; // 0-私有，1-公有，     2-指定用户群拥有（待实现

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }

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
