package com.lmyxlf.jian_mu.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/27 2:35
 * @description 资源文件配置加载
 * @since 17
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /**
     * 系统语言
     */
    private static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    @Bean
    public LocaleResolver localeResolver() {

        SessionLocaleResolver slr = new SessionLocaleResolver();
        // 默认语言
        slr.setDefaultLocale(DEFAULT_LOCALE);

        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {

        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");

        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(localeChangeInterceptor());
    }
}