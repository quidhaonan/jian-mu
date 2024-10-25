package com.lmyxlf.jian_mu.admin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 18:49
 * @description excel 注解集
 * @since 17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excels {

    Excel[] value();
}