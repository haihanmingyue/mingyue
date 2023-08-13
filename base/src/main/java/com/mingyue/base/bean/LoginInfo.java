package com.mingyue.base.bean;

import com.mingyue.userrole.bean.UserMenuBean;
import org.apache.shiro.session.Session;

import java.util.List;

public class LoginInfo {
    private String userName;
    private String uuid;
    private String headerPic;
    private String sessionId;

    private List<UserMenuBean> userMenuList;

    public LoginInfo() {

    }
    public LoginInfo(UserAccount account, Session session) {
        if (account != null) {
            setHeaderPic(account.getHeaderPic());
            setUuid(account.getUuid());
            setUserName(account.getUserName());
        }
        if (session != null) {
            setSessionId(session.getId().toString());
        }
    }

    public List<UserMenuBean> getUserMenuList() {
        return userMenuList;
    }

    public void setUserMenuLists(List<UserMenuBean> userMenuList) {
        this.userMenuList = userMenuList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private List<Object> roleList; //权限集合   后续增加

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeaderPic() {
        return headerPic;
    }

    public void setHeaderPic(String headerPic) {
        this.headerPic = headerPic;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Object> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Object> roleList) {
        this.roleList = roleList;
    }
}
