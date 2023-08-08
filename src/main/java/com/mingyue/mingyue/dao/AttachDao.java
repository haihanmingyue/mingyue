package com.mingyue.mingyue.dao;


import com.mingyue.mingyue.bean.Attach;
import com.mingyue.mingyue.bean.AttachType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttachDao extends BaseDao<Attach> {

    List<Attach> findBySubType(Map<String,?> params);
}
