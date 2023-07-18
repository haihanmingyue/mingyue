package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.User;


import com.mingyue.mingyue.utils.MapUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


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

    /**
     * 基于@ExceptionHandler的统一异常处理，返回json数据
     * 拦截spring参数验证错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map exp(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        String msg = "参数错误";
        e.getBindingResult().getAllErrors();
        if(e.getBindingResult().getAllErrors().size() > 0){
            msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        Map d = MapUtil.genMap("code", HttpStatus.INTERNAL_SERVER_ERROR,"msg", msg, "data",null,"version", 1.0);
        return d;
    }
}
