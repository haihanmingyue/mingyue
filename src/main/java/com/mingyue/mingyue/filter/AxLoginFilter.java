package com.mingyue.mingyue.filter;



import com.mingyue.mingyue.config.Config;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;


//@WebFilter //本地用这个有用，部署到tomcat上失效，估计得是jar包才能正常
@Component //还是用这个
public class AxLoginFilter extends AdviceFilter implements ApplicationRunner {

    private static Set<String> excludeUrls = new HashSet<>();
    private static Set<String> authorityUrls = null;
    private static Set<Long> menuIds = null;

    private static String s = "";


    @Value("${config.addservice}")
    private String addService;

    @Value("${config.serviceName}")
    private String serviceName;

    public void setAddService(String addService) {
        this.addService = addService;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddService() {
        return addService;
    }

    public String getServiceName() {
        return serviceName;
    }

    private boolean needAddRole = false;
    public AxLoginFilter(){
        System.err.println("axLoginFilter is start");
    }


    /**
     * 重置缓存
     *
     * @author 钱凯
     * @date 2023-02-12 14:57:43
     */
    public void recacheAuthInfo(){

        authorityUrls = new HashSet<>();
        menuIds = new HashSet<>();
        if (Boolean.parseBoolean(addService)) {
            s = "/" + serviceName;
        }

        excludeUrls.add( s + "/hello/upload");
        excludeUrls.add( s + "/hello/download");
        excludeUrls.add( s + "/hello/method");
        excludeUrls.add( s + "/hello/insert");
    }



    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        Integer clientType = null;
        Long menuId = null;

        String uri = req.getRequestURI();



        //TODO 应该要判断特定的url允许跨域，目前是都true
        if(req.getMethod().equals("OPTIONS")) {
            System.err.println(req.getRequestURI());
            HttpServletResponse rep = (HttpServletResponse) response;
            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);
            rep.setHeader("Access-Control-Allow-Headers", "*");
            rep.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            return true;
        }

        //不需要认证的api
        if(checkUrl(uri)) {
            HttpServletResponse rep = (HttpServletResponse) response;
            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);
            return true;
        }

//        //认证
//        Long humanId = ContextUtils.getCurrentHumanId();
//        if(null == humanId || humanId == 0){
//            HttpServletResponse rep = (HttpServletResponse) response;
//            rep.setStatus(HttpStatus.SC_UNAUTHORIZED);
//            return false;
//        }
        HttpServletResponse rep = (HttpServletResponse) response;
//        rep.setStatus(HttpStatus.UNAUTHORIZED.value());
        return true;
    }

    private boolean checkUrl(String url){
        System.err.println("visit url->" + url);
        for (String excludeUrl : excludeUrls) {
            if(url.matches(excludeUrl)){
                return true;
            }
        }
        return false;
    }

    /**
     * 容器初始化完成后执行的操作
     *
     * */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        recacheAuthInfo();

    }
}
