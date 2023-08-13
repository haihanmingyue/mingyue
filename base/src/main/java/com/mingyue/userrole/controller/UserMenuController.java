package com.mingyue.userrole.controller;

import com.mingyue.base.bean.ReturnBean;
import com.mingyue.base.controller.BaseController;
import com.mingyue.userrole.bean.UserMenu;
import com.mingyue.userrole.dao.UserMenuDao;
import com.mingyue.userrole.service.UserMenuServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("userMenu")
public class UserMenuController extends BaseController<UserMenu, UserMenuDao,UserMenuServices> {


    @Autowired
    private UserMenuServices services;


    @RequestMapping("/getRoleList")
    @ResponseBody
    public ReturnBean getRoleList() {
        return ReturnBean.ok("查询成功").setData(services.getRoleList());
    }

    @Override
    protected UserMenuServices getService() {
        return services;
    }
}
