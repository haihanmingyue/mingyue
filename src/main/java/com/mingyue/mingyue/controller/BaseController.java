package com.mingyue.mingyue.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.mingyue.bean.BaseBean;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.User;


import com.mingyue.mingyue.dao.BaseDao;
import com.mingyue.mingyue.service.BaseService;
import com.mingyue.mingyue.utils.MapUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public abstract class BaseController<T extends BaseBean,
        D extends BaseDao<T>,
        S extends BaseService<T,D>

> extends ThrowExceptionController implements HttpRequestHandler {

    protected abstract S getService();

    public S getService(String viewId) {
        return this.getService();
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    @RequestMapping("/list")
    @ResponseBody
    public ReturnBean list(HttpServletRequest request,HttpServletResponse response) {

        Map<String, String> param = MapUtil.getRequestParamsMap(request);
        PageHelper.startPage(param);
        List<T> list = getService().findByWhere(param);
        PageInfo<T> pageInfo = new PageInfo<>(list);

        return ReturnBean.ok("查询成功").setData(pageInfo);

    }
}
