package com.mingyue.mingyue.jedis;


import com.mingyue.mingyue.factory.YmlFactory;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "jedis.yml", factory = YmlFactory.class)
@ConfigurationProperties(prefix = "jedis.pool")
@Data
public class JedisProperties {
    private int maxTotal;
    private int maxIdle;
    private int maxWaitMillis;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private int timeBetweenEvictionRunsMillis;
    private boolean testWhileIdle;
    private int numTestPerEvictionRun;
    private int minIdle;
    private String host;
    private String password;
    private int port;
    private int timeout;

}
