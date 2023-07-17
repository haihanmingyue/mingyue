package com.mingyue.mingyue.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    public static final String ITEM_TOPIC_EXCHANGE = "item_topic_exchange";

    public static final String HUMAN_TOPIC_EXCHANGE = "human_topic_exchange";

    public static final String HAPPY_TOPIC_EXCHANGE = "happy_topic_exchange";
    //队列名称
    public static final String ITEM_QUEUE = "item_queue";

    public static final String HUMAN_QUEUE = "human_queue";

    public static final String HAPPY_QUEUE = "happy_queue";

    //声明交换机
    @Bean("itemTopicExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
    }
    //声明队列
    @Bean("itemQueue")
    public Queue itemQueue(){
        return QueueBuilder.durable(ITEM_QUEUE).build();
    }


    //声明交换机
    @Bean("humanTopicExchange")
    public Exchange humanTopicExchange(){
        return ExchangeBuilder.topicExchange(HUMAN_TOPIC_EXCHANGE).durable(true).build();
    }
    @Bean("humanQueue")
    public Queue humanQueue(){
        return QueueBuilder.durable(HUMAN_QUEUE).build();
    }

    //声明交换机
    @Bean("happyTopicExchange")
    public Exchange happyTopicExchange(){
        return ExchangeBuilder.topicExchange(HAPPY_TOPIC_EXCHANGE).durable(true).build();
    }
    @Bean("happyQueue")
    public Queue happyQueue(){
        return QueueBuilder.durable(HAPPY_QUEUE).build();
    }



    //绑定队列和交换机
    @Bean
    public Binding humanQueueExchange(@Qualifier("humanQueue") Queue queue,
                                     @Qualifier("humanTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("human.#").noargs();
    }

    //绑定队列和交换机
    @Bean
    public Binding happyQueueExchange(@Qualifier("happyQueue") Queue queue,
                                     @Qualifier("happyTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("happy.#").noargs();
    }

    //绑定队列和交换机
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemQueue") Queue queue,
                                     @Qualifier("itemTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }

}
