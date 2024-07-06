package com.lmyxlf.jian_mu.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class EnableWebFluxConfig {

    private static final Logger log = LoggerFactory.getLogger(EnableWebFluxConfig.class);

    public EnableWebFluxConfig() {
        if (log.isDebugEnabled()) {
            log.debug("检测到为Reactive容器类型,注入相关Bean对象");
        }
    }

    @Bean
    public WebFluxIpFilter webFluxIpFilter(){
        return new WebFluxIpFilter();
    }
}
