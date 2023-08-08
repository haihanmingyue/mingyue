package com.mingyue.userrole.service;


import com.mingyue.mingyue.service.BaseService;
import com.mingyue.userrole.bean.UserMenu;
import com.mingyue.userrole.bean.UserMenuBean;
import com.mingyue.userrole.dao.UserMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserMenuServices extends BaseService<UserMenu, UserMenuDao> {

    @Autowired
    private UserMenuDao dao;

    @Override
    public UserMenuDao getDao() {
        return dao;
    }



    /**
     * 获取当前用户权限表
     *
     * */
    public List<UserMenuBean> getRoleList() {


        return null;
    }
}
