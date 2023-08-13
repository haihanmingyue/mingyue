package com.mingyue.mingyue.log4j;


import com.alibaba.fastjson2.JSONObject;
import com.mingyue.mingyue.interfacePack.LogInter;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


@Aspect
@Component
public class LogAspect {


    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.mingyue.mingyue.interfacePack.LogInter)")
    public void logInter(){
        
    }
    /**
     * 环绕通知
     * @param joinPoint
     * @param id
     * @return
     * @throws Throwable
     */
    @Around("logInter() && @annotation(id)")
    public Object around(ProceedingJoinPoint joinPoint, LogInter id) throws Throwable {
        try {
            System.err.println("-------------around start-------------");
            Object[] objects = joinPoint.getArgs();

            Class<?> pClass = id.setClass();

            for (Object o : objects) {
                if (o != null) {
                    if (o instanceof RequestFacade) {
                        HttpServletRequest request = (HttpServletRequest) o;

                        Enumeration<String> enumeration = request.getParameterNames();
                        while (enumeration.hasMoreElements()) {
                            String key = enumeration.nextElement();
                            System.err.println("key-> " + key);
                            System.err.println(request.getParameter(key));
                        }

                    } else if (o instanceof ResponseFacade) {

                        HttpServletResponse response = (HttpServletResponse) o;

                    } else if (o instanceof StandardMultipartHttpServletRequest) {

                        System.err.println("file");
                    } else if (o.getClass() == pClass) {

                        System.err.println(JSONObject.toJSONString(o));

                    } else {
                        System.err.println(o.getClass().getName());
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return joinPoint.proceed();
    }

    /**
     * 前置通知
     * @param joinPoint
     * @param id
     */
    @Before("logInter()  && @annotation(id)")
    public void before(JoinPoint joinPoint, LogInter id){

    }

    /**
     * 后置通知
     * @param joinPoint
     * @param id
     */
    @After("logInter() && @annotation(id)")
    public void after(JoinPoint joinPoint,LogInter id){

    }

    /**
     * 后置返回通知
     * @param joinPoint
     * @param id
     */
    @AfterReturning("logInter() && @annotation(id)")
    public void afterReturn(JoinPoint joinPoint, LogInter id){

    }

    /**
     * 后置异常通知
     * @param joinPoint
     * @param id
     */
    @AfterThrowing("logInter() && @annotation(id)")
    public void afterThrow(JoinPoint joinPoint,LogInter id){

    }
    
    
}
