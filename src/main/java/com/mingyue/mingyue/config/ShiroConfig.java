package com.mingyue.mingyue.config;



import com.mingyue.mingyue.filter.AxLoginFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mingyue.mingyue.shiro.Realm;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroConfig
 * 2023年8月6日19:39:43
 * hhmy
 *
 * */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给ShiroFilter配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);


        // 放入过滤器
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("axLoginFilter", new AxLoginFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/**", "axLoginFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);


        return shiroFilterFactoryBean;
    }
    //创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }
    //创建自定义Realm
    @Bean
    public Realm getRealm() {
        return new Realm();
    }
}
