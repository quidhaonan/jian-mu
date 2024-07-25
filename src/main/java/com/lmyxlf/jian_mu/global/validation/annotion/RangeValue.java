package com.lmyxlf.jian_mu.global.validation.annotion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/26 0:54
 * @description 自定义校验规则，校验所传参数是否在数组中
 * @since 17
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = RangeValidator.class)
public @interface RangeValue {

    int[] value();

    String message() default "不支持的参数";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}