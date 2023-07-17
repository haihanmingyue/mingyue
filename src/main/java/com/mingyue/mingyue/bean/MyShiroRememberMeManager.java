package com.mingyue.mingyue.bean;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;

public class MyShiroRememberMeManager extends CookieRememberMeManager {
    public MyShiroRememberMeManager() {
    }

    public void onSuccessfulLogin(Subject subject, AuthenticationToken token, AuthenticationInfo info) {
        super.onSuccessfulLogin(subject, token, info);
    }
}