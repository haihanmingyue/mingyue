package com.mingyue.mingyue.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapUtil {
    public static Map<String,Object> genMap(Object ...keysAndValues) {
        Map<String, Object> ret = new HashMap<>();
        int len = keysAndValues.length;
        if (len == 0) {
            return ret;
        } else if (len % 2 != 0) {
            throw new IllegalArgumentException("传入的参数必须成对!");
        } else {
            for(int i = 0; i < len; i += 2) {
                String key = String.valueOf(keysAndValues[i]);
                Object val = keysAndValues[i + 1];
                if (val != null) {
                    ret.put(key, val);
                }
            }

            return ret;
        }
    }

    public static Map<String,String> parseMap(String text) {
        Map<String,String> map = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(text);
        for (String key : jsonObject.keySet()) {
            map.put(key,jsonObject.getString(key));
        }
        return map;
    }

    public static Map<String,String> getRequestParamsMap(HttpServletRequest request){
        Set<String> keysSet = request.getParameterMap().keySet();
        Map<String,String> params = new HashMap<>();
        for(String key : keysSet) {
            String value = request.getParameter(key);
            if (value != null && value.length() == 0)//排除空串
                continue;
            params.put(key, value);
        }
        return params;
    }
}