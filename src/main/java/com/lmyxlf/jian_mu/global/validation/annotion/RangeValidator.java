package com.lmyxlf.jian_mu.global.validation.annotion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/26 0:54
 * @description
 * @since 17
 */
public class RangeValidator implements ConstraintValidator<RangeValue, Integer> {

    private int[] array;

    @Override
    public void initialize(RangeValue rangeValue) {
        this.array = rangeValue.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int val = value;
        for (int i : array) {
            if (val == i) {
                return true;
            }
        }
        return false;
    }
}