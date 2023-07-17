package com.mingyue.mingyue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


@SpringBootApplication(scanBasePackages = {"com.*.*.*"})
//@MapperScan(basePackages = {"com.*.*.dao"})   // springboot版本问题 版本要和 mybatis能对应上
//@ServletComponentScan(basePackages = {"com.*.*.filter"}) // springboot版本问题
// 换成springboot 2 就可以了  javax.servlet.annotation.WebFilter;
// springboot 3以后 扫描的是  jakata.servlet.annotation.WebFilter;的@WebFilter注解 而且要实现 jakata 下面的Filter
public class MingyueApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MingyueApplication.class, args);
    }


    //extends SpringBootServletInitializer 重写 configure 才能正常在tomcat中运行 否则所有接口404，新创个类继承也行，不一定写在这
    // 不写这个的话估计打成jar包才能正常运行
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MingyueApplication.class);
    }
}
