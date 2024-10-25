package com.lmyxlf.jian_mu.admin.annotation;

import com.lmyxlf.jian_mu.admin.handler.XssValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:41
 * @description 自定义 xss 校验注解
 * @since 17
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {XssValidator.class})
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface Xss {

    String message() default "不允许任何脚本运行";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}