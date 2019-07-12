package com.tongbao.log;

import com.tongbao.log.server.LogConsumerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author xiaohaifang
 * @date 2019/7/11 17:07
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages={"com.tongbao.log.*"})
@EnableAsync
public class LogMain {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LogMain.class, args);
        LogConsumerServer logConsumerServer = (LogConsumerServer) context.getBean("logConsumerServer");
        logConsumerServer.consumeLog();
    }
}
