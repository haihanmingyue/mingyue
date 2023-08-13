package com.mingyue.mingyue.factory;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class Factory implements ApplicationContextAware {

//    private static final Map<Integer,Father> factoryInterFaceMap = new HashMap<>();


//    public static Father getBean(Integer type,Integer xx) {
//        Father father = factoryInterFaceMap.get(type);
//        father.setNumber(xx);
//        return father;
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        Map<String,Father> map = applicationContext.getBeansOfType(Father.class);
//        map.values().forEach(factoryInterFace -> {
//            factoryInterFaceMap.put(factoryInterFace.getType(),factoryInterFace);
//        });
    }
}
