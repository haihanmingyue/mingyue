package com.mingyue.base.dao;



import com.mingyue.base.bean.UserAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;



@Mapper //用注解
public interface TestDao {

    List<String> test(Map<String,Object> params);


    List<UserAccount> findByWhere(Map<String,Object> params);

    void insert(Map<String,Object> params);

    void batchInsert(List<UserAccount> list);

    String getContent();
}
