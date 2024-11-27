package com.lmyxlf.jian_mu.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/24 21:16
 * @description 请求路径注解，标识该请求允许匿名访问
 * @since 17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreAuthUrlAnnotation {

}