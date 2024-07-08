package com.lmyxlf.jian_mu.global.config;

// import com.lmyxlf.jian_mu.global.interceptor.GatewayInterceptor;

import com.lmyxlf.jian_mu.global.interceptor.LogRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
public class LogConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LogRequestInterceptor logRequestInterceptor = new LogRequestInterceptor();
        registry.addInterceptor(logRequestInterceptor).addPathPatterns("/**");
        // GatewayInterceptor gatewayInterceptor = new GatewayInterceptor();
        // registry.addInterceptor(gatewayInterceptor).addPathPatterns("/**");
    }
}