package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.AttachSubType;
import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.service.AttachSubTypeServices;
import com.mingyue.mingyue.service.UserAccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("attachSubType")
public class AttachSubTypeController extends BaseController{



    @Autowired
    private AttachSubTypeServices attachSubTypeServices;



    @RequestMapping("/save")
    @ResponseBody
    public ReturnBean save(@RequestBody @Validated AttachSubType attachSubType) {
        attachSubTypeServices.save(attachSubType);
        return ReturnBean.ok("更新成功").setData("success");
    }

}
