package com.mingyue.mingyue.config;

import io.netty.util.internal.StringUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class JeDisPoolConfig {

    @Bean
    public JedisPool jeDisPool(JedisProperties jedisProperties) {

        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(jedisProperties.getMaxTotal());
        config.setMaxIdle(jedisProperties.getMaxIdle());
        config.setMinIdle(jedisProperties.getMinIdle());
        config.setMaxWait(Duration.ofMillis(jedisProperties.getMaxWaitMillis()));
//        config.setTestOnBorrow(jedisProperties.isTestOnBorrow());
//        config.setTestOnReturn(jedisProperties.isTestOnReturn());
//        config.setTimeBetweenEvictionRuns(Duration.ofMillis(jedisProperties.getTimeBetweenEvictionRunsMillis()));
//        config.setTestWhileIdle(jedisProperties.isTestWhileIdle());
//        config.setNumTestsPerEvictionRun(jedisProperties.getNumTestPerEvictionRun());
        //这里注释掉的配置有部分参数有问题，会导致无法获取redis池
        if (StringUtils.hasText(jedisProperties.getPassword())) {
            return new JedisPool(config,jedisProperties.getHost(),jedisProperties.getPort(),
                    jedisProperties.getTimeout(),jedisProperties.getPassword());
        }

        return new JedisPool(config,jedisProperties.getHost(),jedisProperties.getPort(),jedisProperties.getTimeout());
    }
}
