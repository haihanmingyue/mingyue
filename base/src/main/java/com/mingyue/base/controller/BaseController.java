package com.mingyue.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.base.bean.BaseBean;
import com.mingyue.base.bean.ReturnBean;


import com.mingyue.base.dao.BaseDao;
import com.mingyue.base.service.BaseService;
import com.mingyue.mingyue.utils.MapUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/save")
    @ResponseBody
    public ReturnBean save(@RequestBody @Validated T bean) {
        getService().save(bean);
        return ReturnBean.ok("更新成功").setData("success");
    }
}
