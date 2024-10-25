package com.lmyxlf.jian_mu.admin.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/20 2:14
 * @description bean 对象属性验证
 * @since 17
 */
public class BeanValidatorsUtil {

    public static void validateWithException(Validator validator, Object object, Class<?>... groups)
            throws ConstraintViolationException {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}