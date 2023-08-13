package com.mingyue.mingyue.netty;


import com.mingyue.mingyue.factory.YmlFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:netty.yml", factory = YmlFactory.class)
@ConfigurationProperties(prefix = "netty")

public class NettyConfig {
}
