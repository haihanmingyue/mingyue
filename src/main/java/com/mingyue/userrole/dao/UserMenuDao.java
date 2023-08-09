package com.mingyue.userrole.dao;

import com.mingyue.mingyue.dao.BaseDao;
import com.mingyue.userrole.bean.UserMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMenuDao extends BaseDao<UserMenu> {
    List<UserMenu> findByUser(String userUuid);
}
