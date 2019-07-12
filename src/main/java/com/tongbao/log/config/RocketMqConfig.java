package com.tongbao.log.config;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author xiaohaifang
 * @date 2019/6/13 14:10
 */
@Component
@ConfigurationProperties(prefix = "rocketmq")
@Configuration
@Data
public class RocketMqConfig {

    private String accessKey;
    private String secretKey;
    private String sendMsgTimeoutMillis;
    private String nameSrvAddr;

    private String logGroupId;
    private int logConsumerThreadNums;
    private String logTopic;
    private String logTags;

    /**
     * 日志消费者
     * @return
     */
    @Bean(name="logConsumer")
    public Consumer applyConsumer() {
        return getConsumer(logGroupId, logConsumerThreadNums);
    }

    /**
     * 申请生产者
     * @return
     */
    @Bean(name="logProducer", initMethod = "start", destroyMethod = "shutdown")
    public Producer applyProducer() {
        return getProducer(logGroupId);
    }
    /**
     * 消费者对象
     * @param groupId
     * @param consumerThreadNums
     * @return
     */
    
    private Consumer getConsumer(String groupId, int consumerThreadNums) {
        Properties properties = new Properties();
        // 您在控制台创建的 Group ID
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,accessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,nameSrvAddr);
        properties.put(PropertyKeyConst.ConsumeThreadNums,consumerThreadNums);
        return ONSFactory.createConsumer(properties);
    }

    /**
     * 生产者
     * @param groupId
     * @return
     */
    
    
    private Producer getProducer(String groupId) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,accessKey);
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, sendMsgTimeoutMillis);
        // 设置 TCP 接入域名，到控制台的实例基本信息中查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR,nameSrvAddr);

        Producer producer = ONSFactory.createProducer(properties);
//        producer.start();
        return producer;
    }

}
