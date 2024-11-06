package com.lmyxlf.jian_mu.global.config;

import com.lmyxlf.jian_mu.global.interceptor.LogParamFilter;
import com.lmyxlf.jian_mu.global.runner.GcInitRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description spring 相关配置
 * @since 17
 */
public class SpringConfig {

    @Bean
    public GcInitRunner gcInitRunner() {
        return new GcInitRunner();
    }

    // @Bean
    // public AuthFeignInterceptor authFeignInterceptor() {
    //     return new AuthFeignInterceptor();
    // }

    @Bean
    public FilterRegistrationBean<LogParamFilter> registFilter() {

        FilterRegistrationBean<LogParamFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogParamFilter());
        registration.addUrlPatterns("/*");
        registration.setName("LogFilter");
        registration.setOrder(1);

        return registration;
    }
}
