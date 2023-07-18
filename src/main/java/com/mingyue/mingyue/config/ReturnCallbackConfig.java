package com.mingyue.mingyue.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ReturnCallbackConfig implements RabbitTemplate.ReturnsCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct // @PostContruct是spring框架的注解，在⽅法上加该注解会在项⽬启动的时候执⾏该⽅法，也可以理解为在spring容器初始化的时候执
    public void init() {
        rabbitTemplate.setReturnsCallback(this);
    }


    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.println("returnCallback ..............");
        System.out.println(returnedMessage.getMessage());// 消息本身
        System.out.println(returnedMessage.getReplyCode());// index
        System.out.println(returnedMessage.getReplyText());
        System.out.println(returnedMessage.getExchange());
        System.out.println(returnedMessage.getRoutingKey());
    }

}
