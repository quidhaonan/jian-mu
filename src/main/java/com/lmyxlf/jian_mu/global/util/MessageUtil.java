package com.lmyxlf.jian_mu.global.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/22 4:11
 * @description 获取 i18n 资源文件
 * @since 17
 */
public class MessageUtil {

    /**
     * 根据消息键和参数 获取消息 委托给 spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {

        MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}