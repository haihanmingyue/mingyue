package com.mingyue.userrole.bean;

import java.util.*;

public class UserMenuBean {

    private String name;

    private String path;

    private String nameCn;

    private String component;

    private String fatherId;





    private List<UserMenuBean> childrenList;


    public UserMenuBean() {

    }

    public UserMenuBean(SysMenu sysMenu) {
        if (sysMenu != null) {
            this.name = sysMenu.getName();
            this.path = sysMenu.getPath();
            this.nameCn = sysMenu.getNameCn();
            this.component = sysMenu.getComponent();
            this.childrenList = new ArrayList<>();
            this.fatherId = sysMenu.getFatherId();
        }
    }



    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

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
