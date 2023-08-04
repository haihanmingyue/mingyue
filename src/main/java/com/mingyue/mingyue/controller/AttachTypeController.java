package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.service.AttachTypeServices;
import com.mingyue.mingyue.service.UserAccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("attachType")
public class AttachTypeController extends BaseController{



    @Autowired
    private AttachTypeServices attachTypeServices;



    @RequestMapping("/save")
    @ResponseBody
    public ReturnBean save(@RequestBody @Validated AttachType attachType) {
        attachTypeServices.save(attachType);
        return ReturnBean.ok("更新成功").setData("success");
    }

}
