package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.User;


import com.mingyue.mingyue.utils.MapUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class BaseController implements HttpRequestHandler {

    public Logger logger = Logger.getLogger(this.getClass());

    /**
     * 基于@ExceptionHandler的统一异常处理，返回json数据
     * 拦截spring参数验证错误
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ReturnBean exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        String errorMsg = "服务器内部错误，请稍后再试";
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            List<ObjectError> list = e.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = error.getDefaultMessage();
                logger.error(error.getDefaultMessage());
                break;
            }
        }
        else if (ex instanceof RuntimeException) {
            RuntimeException e = (RuntimeException) ex;
            logger.error(e.getMessage());
            errorMsg = e.getMessage();
        }
        else {
            logger.error(ex);
        }
        return ReturnBean.error(errorMsg);
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
