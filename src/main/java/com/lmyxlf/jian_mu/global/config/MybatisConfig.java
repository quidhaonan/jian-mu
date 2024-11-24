package com.lmyxlf.jian_mu.global.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lmyxlf.jian_mu.global.handler.AutoMetaFieldHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@EnableTransactionManagement
@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // mysql
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public AutoMetaFieldHandler autoMetaFieldHandler(){

        return new AutoMetaFieldHandler();
    }
}