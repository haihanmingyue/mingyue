package com.mingyue.mingyue.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfirmCallbackConfig implements RabbitTemplate.ConfirmCallback{
    // 为确保消息发送的准确性，设置发布时确认，确认消息是否到达 Broker 服务器 消息只要被Broker接收,就会触发

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct // @PostContruct是spring框架的注解，在⽅法上加该注解会在项⽬启动的时候执⾏该⽅法，也可以理解为在spring容器初始化的时候执行
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) { // 消息投递到broker 的状态，true表示成功
            System.out.println("消息发送到Broker成功！");
        } else { // 发送异常
            System.out.println("发送异常原因 = " + cause);
        }

    }
}
