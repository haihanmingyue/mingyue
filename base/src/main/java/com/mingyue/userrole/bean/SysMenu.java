package com.mingyue.userrole.bean;

import com.mingyue.base.bean.BaseBean;

public class SysMenu extends BaseBean {

    private String roleType; // 作用类型 1-页面路由，2-接口地址,3-按钮权限   页面路由和按钮权限返回给前端，接口地址不返回，写个aop拦截查看权限，接口权限list可以存redis里

    private String path; // 浏览器路径 对应前端

    private String name; // 名字 对应前端

    private String component; // 文件路径 对应前端

    private String code; // 路径 代号

    private String nameCn;

    private String urlLevel; // 路径等级 0-是父节点，。。。依次类推

    private String fatherId; //父节点的 uuid

    private String isAllMenu;// 是否是公有menu，共有权限不需要授权

    private Integer isTitleMenu = 0;

    public Integer getIsTitleMenu() {
        return isTitleMenu;
    }

    public void setIsTitleMenu(Integer isTitleMenu) {
        this.isTitleMenu = isTitleMenu;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getIsAllMenu() {
        return isAllMenu;
    }

    public void setIsAllMenu(String isAllMenu) {
        this.isAllMenu = isAllMenu;
    }

    public String getRoleType() {
        return roleType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
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

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getUrlLevel() {
        return urlLevel;
    }

    public void setUrlLevel(String urlLevel) {
        this.urlLevel = urlLevel;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }
}
