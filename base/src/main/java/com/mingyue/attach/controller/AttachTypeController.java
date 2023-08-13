package com.mingyue.attach.controller;

import com.mingyue.attach.bean.AttachType;
import com.mingyue.base.bean.ReturnBean;
import com.mingyue.base.controller.BaseController;
import com.mingyue.attach.dao.AttachTypeDao;
import com.mingyue.attach.services.AttachTypeServices;
import com.mingyue.mingyue.utils.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("attachType")
public class AttachTypeController extends BaseController<AttachType, AttachTypeDao, AttachTypeServices> {



    @Autowired
    private AttachTypeServices attachTypeServices;


    @Override
    public ReturnBean list(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        List<AttachType> list =  attachTypeServices.findByWhere(MapUtil.genMap());
        return ReturnBean.ok("查询成功").setData("success").setData(MapUtil.genMap("rows",list,"total",list.size()));
    }


    @Override
    protected AttachTypeServices getService() {
        return attachTypeServices;
    }
}
