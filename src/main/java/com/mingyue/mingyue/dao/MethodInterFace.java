package com.mingyue.mingyue.dao;

import org.springframework.web.bind.ServletRequestBindingException;

/**
 * 策略
 *
 * */


public interface MethodInterFace {

    String initType();

    String method(Object params) throws ServletRequestBindingException;

}