package com.lmyxlf.jian_mu.admin.annotation;

import java.lang.annotation.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 22:18
 * @description 数据权限过滤注解
 * @since 17
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

    /**
     * 部门表的别名
     */
    String deptAlias() default "";

    /**
     * 用户表的别名
     */
    String userAlias() default "";

    /**
     * 权限字符（用于多个角色匹配符合要求的权限）默认根据权限注解 @ss 获取，多个权限用逗号分隔开来
     */
    String permission() default "";
}