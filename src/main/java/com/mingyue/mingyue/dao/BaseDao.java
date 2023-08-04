package com.mingyue.mingyue.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

   void create(T bean);

   void batchCreate(List<T> bean);

   T get(String uuid);

   List<T> findByWhere(Map<String,?> params);

   T findOneByWhere(Map<String,?> params);
}
