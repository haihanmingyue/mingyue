package com.mingyue.mingyue.shiro;

import com.mingyue.base.bean.UserAccount;
import com.mingyue.base.service.UserAccountServices;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class Realm extends AuthorizingRealm  {
    public Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserAccountServices userAccountServices;



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        logger.info("授权AuthorizationInfo");

        SimpleAuthorizationInfo  info = new SimpleAuthorizationInfo ();
        Subject subject = SecurityUtils.getSubject(); //拿到user对象
        UserAccount account = (UserAccount) subject.getPrincipal();
        List<String> ps = new ArrayList<>();

        //放东西。
        info.addStringPermissions(ps);

       return info;

    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("认证AuthenticationInfo");
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        UserAccount userAccount = userAccountServices.findByUsername(token.getUsername());

        SimpleAuthenticationInfo retAu;
        if (userAccount == null) {
            throw new RuntimeException("账号不存在");

        } else {
            retAu = new SimpleAuthenticationInfo(userAccount, userAccount.getPassWord(), this.getName());
        }
        return retAu;
    }
}
