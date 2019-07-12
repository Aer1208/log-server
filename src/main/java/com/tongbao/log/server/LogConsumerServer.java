package com.tongbao.log.server;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import com.tongbao.log.config.RocketMqConfig;
import com.tongbao.log.mail.MailSenderServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author xiaohaifang
 * @date 2019/7/11 17:16
 */
@Component("logConsumerServer")
@Slf4j
public class LogConsumerServer {

    @Autowired
    private Consumer logConsumer;

    @Autowired
    private RocketMqConfig rocketMqConfig;

    @Autowired
    private MailSenderServer mailSenderServer;

    public void consumeLog() {
        log.info("开始启动消费者");

        logConsumer.subscribe(rocketMqConfig.getLogTopic(), rocketMqConfig.getLogTags(), new MessageListener() { //订阅多个 Tag
            public Action consume(Message message, ConsumeContext context) {
                log.info("接受到消息：" + new String(message.getBody()));
                JSONObject jsonObject = JSON.parseObject(new String(message.getBody()));
                mailSenderServer.sendMail(jsonObject);
                return Action.CommitMessage;
            }
        });
        logConsumer.start();
        log.info("LogConsumer is start!");
    }
}
