package com.mingyue.userrole.bean;

import java.util.List;

public class UserMenuBean {

    private String name;

    private String path;

    private String nameCn;

    private String component;

    private List<UserMenuBean> childrenList;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<UserMenuBean> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<UserMenuBean> childrenList) {
        this.childrenList = childrenList;
    }
}
