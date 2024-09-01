package com.lmyxlf.jian_mu.log;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class LogWebMvcConfig implements WebMvcConfigurer {

    private final RemoteIpLogInterceptor remoteIpLogInterceptor;

    public LogWebMvcConfig() {
        this.remoteIpLogInterceptor = new RemoteIpLogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(remoteIpLogInterceptor);
    }
}