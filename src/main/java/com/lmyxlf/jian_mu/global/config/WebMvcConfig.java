package com.lmyxlf.jian_mu.global.config;

import com.lmyxlf.jian_mu.global.interceptor.SpringMvcSessionBindInterceptor;
import com.lmyxlf.jian_mu.global.mvc.JsonParamModelAttributeMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description springMcv 自定义的一些配置
 * @since 17
 */
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 向容器中添加自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 该拦截器用于绑定全局参数
        registry.addInterceptor(new SpringMvcSessionBindInterceptor());
    }

    /**
     * 向容器中添加自定义 json 参数解析器
     *
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new JsonParamModelAttributeMethodProcessor());
    }
}