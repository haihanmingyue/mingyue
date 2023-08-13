package com.mingyue.attach.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.attach.bean.AttachSubType;
import com.mingyue.base.bean.ReturnBean;
import com.mingyue.base.controller.BaseController;
import com.mingyue.attach.dao.AttachSubTypeDao;
import com.mingyue.attach.services.AttachSubTypeServices;
import com.mingyue.mingyue.utils.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("attachSubType")
public class AttachSubTypeController extends BaseController<AttachSubType, AttachSubTypeDao,AttachSubTypeServices> {



    @Autowired
    private AttachSubTypeServices attachSubTypeServices;

    @Override
    public ReturnBean list(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = MapUtil.getRequestParamsMap(request);
        PageHelper.startPage(1,999);
        List<AttachSubType> list =  attachSubTypeServices.findByWhere(map);
        PageInfo pageInfo = new PageInfo(list);
        return ReturnBean.ok("查询成功").setData("success").setData(MapUtil.genMap("rows",pageInfo.getList(),"total",pageInfo.getTotal()));
    }

    @Override
    protected AttachSubTypeServices getService() {
        return attachSubTypeServices;
    }
}
