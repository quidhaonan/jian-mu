package com.lmyxlf.jian_mu.log;

import com.ctrip.framework.apollo.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
@Slf4j
@Configuration
@ConditionalOnClass(Config.class)
@ConditionalOnProperty(value = "apollo.bootstrap.enabled", havingValue = "true")
public class EnableApolloLogConfig {

    @Bean
    public LogListenerConfig logListenerConfig(LoggingSystem loggingSystem) {
        return new LogListenerConfig(loggingSystem);
    }
}