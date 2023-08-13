package com.mingyue.base.dao;


import com.mingyue.base.bean.UserAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccountDao extends BaseDao<UserAccount>{
    //根据用户名查找用户
    UserAccount findByUsername(String username);
}
