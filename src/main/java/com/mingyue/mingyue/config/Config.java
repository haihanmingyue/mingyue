package com.mingyue.mingyue.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.unit.DataSize;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;


/**
 * 通过配置类注册
 *
 * */
@Configuration
public class Config implements WebMvcConfigurer {


    public static String upload ;

    @Value("${file.upload.path}")
    public void setUpload(String upload) {
        Config.upload = upload;
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("10240000KB"));// 单个数据大小 1024KB = 1MB
        factory.setMaxRequestSize(DataSize.parse("10240000KB")); // 总上传数据大小
        return factory.createMultipartConfig();
    }
}
