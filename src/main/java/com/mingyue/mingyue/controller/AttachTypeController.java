package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.dao.AttachDao;
import com.mingyue.mingyue.dao.AttachTypeDao;
import com.mingyue.mingyue.service.AttachServices;
import com.mingyue.mingyue.service.AttachTypeServices;
import com.mingyue.mingyue.service.UserAccountServices;
import com.mingyue.mingyue.utils.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("attachType")
public class AttachTypeController extends BaseController<AttachType, AttachTypeDao, AttachTypeServices> {



    @Autowired
    private AttachTypeServices attachTypeServices;



    @RequestMapping("/save")
    @ResponseBody
    public ReturnBean save(@RequestBody @Validated AttachType attachType) {
        attachTypeServices.save(attachType);
        return ReturnBean.ok("更新成功").setData("success");
    }

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
