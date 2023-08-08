package com.mingyue.mingyue.filter;


import com.mingyue.mingyue.utils.BaseContextUtils;
import io.netty.util.internal.StringUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;


//@WebFilter //本地用这个有用，部署到tomcat上失效，估计得是jar包才能正常
//@Component //还是用这个
public class AxLoginFilter extends AdviceFilter {

    public Logger logger = Logger.getLogger(this.getClass());

    private static Set<String> excludeUrls = new HashSet<>();
    private static Set<String> authorityUrls = null;
    private static Set<Long> menuIds = null;

    private static String s = "";

    private boolean needAddRole = false;

    public AxLoginFilter() {
        logger.warn("axLoginFilter is start");

        recacheAuthInfo();
    }


    /**
     * 重置缓存
     *
     * @author 钱凯
     * @date 2023-02-12 14:57:43
     */
    public void recacheAuthInfo() {

        logger.warn("开始初始化");
        authorityUrls = new HashSet<>();
        menuIds = new HashSet<>();

        s = "/mingyue"; //servicesName

        excludeUrls.add(s + "/user/login");
        excludeUrls.add(s + "/user/register");
    }


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        Integer clientType = null;
        Long menuId = null;

        String uri = req.getRequestURI();


        //TODO 应该要判断特定的url允许跨域，目前是都true
//        if (req.getMethod().equals("OPTIONS")) {
////            System.err.println(req.getRequestURI());
//            HttpServletResponse rep = (HttpServletResponse) response;
//            String origin = req.getHeader("Origin");
//            rep.setHeader("Access-Control-Allow-Origin", origin);
//            rep.setHeader("Access-Control-Allow-Headers", "*");
//            rep.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
//            return true;
//        }

        //不需要认证的api
        if (checkUrl(uri)) {
            HttpServletResponse rep = (HttpServletResponse) response;
            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);
            return true;
        }

        //认证
        String humanId = BaseContextUtils.getCurrentHumanId();
        if (StringUtil.isNullOrEmpty(humanId)) {
            HttpServletResponse rep = (HttpServletResponse) response;
            rep.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private boolean checkUrl(String url) {
        logger.warn("visit url->" + url);
        for (String excludeUrl : excludeUrls) {
            if (url.matches(excludeUrl)) {
                return true;
            }
        }
        return false;
    }

}
