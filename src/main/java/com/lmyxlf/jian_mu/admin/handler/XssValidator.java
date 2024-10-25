package com.lmyxlf.jian_mu.admin.handler;

import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.annotation.Xss;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:43
 * @description 自定义 xss 校验注解实现
 * @since 17
 */
public class XssValidator implements ConstraintValidator<Xss, String> {

    private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StrUtil.isBlank(value)) {

            return Boolean.TRUE;
        }

        return !containsHtml(value);
    }

    private static boolean containsHtml(String value) {

        StringBuilder sHtml = new StringBuilder();
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {

            sHtml.append(matcher.group());
        }

        return pattern.matcher(sHtml).matches();
    }
}