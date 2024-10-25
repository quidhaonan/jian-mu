package com.lmyxlf.jian_mu.admin.annotation;

import java.lang.annotation.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 4:03
 * @description 匿名访问不鉴权注解
 * @since 17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Anonymous {

}