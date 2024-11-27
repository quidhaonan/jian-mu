package com.lmyxlf.jian_mu.global.annotation;

import com.lmyxlf.jian_mu.global.constant.SysConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/27 22:00
 * @description 异常通知注解，标识某些类或方法的错误需要额外关注
 * @since 17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NoticeErrorAnnotation {

    /**
     * 标题
     *
     * @return
     */
    String title() default "异常通知";

    /**
     * 异常内容
     *
     * @return
     */
    String content() default "";

    /**
     * 过滤参数
     *
     * @return
     */
    String[] filter() default SysConstant.LMYXLF;
}