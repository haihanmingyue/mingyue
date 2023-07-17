package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.User;


import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;



public class BaseController extends AuthorizingRealm {

    public Logger logger = Logger.getLogger(this.getClass());

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object o = principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        logger.warn("shiro：  "  + o.toString());
// 放入角色信息

// 放入权限信息
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = new User();
        user.setUsername("1");
        user.setPassword("1");
        user.setId(1L);
        if (user == null) {
            throw new RuntimeException("user is error");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
