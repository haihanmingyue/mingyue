package com.mingyue.mingyue.controller;


import com.mingyue.mingyue.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("send")
public class SendMsgController extends BaseController{

    //注入rabbitmq模板
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendmsg")
    @ResponseBody
    public String sendMsg(@RequestParam String msg, @RequestParam String key){
/**
 * 发送消息
 *  参数1：交换机名称
 *  参数2：路由 key
 *  参数3：发送的消息
 */
try {
//    rabbitTemplate.convertAndSend(RabbitmqConfig.ITEM_TOPIC_EXCHANGE,key,msg);
}catch (Exception e) {
    logger.warn("error->", e);
    throw e;
}
//返回消息
        return "发送消息成功";
    }

}
