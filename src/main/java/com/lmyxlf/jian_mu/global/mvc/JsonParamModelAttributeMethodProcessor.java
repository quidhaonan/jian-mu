package com.lmyxlf.jian_mu.global.mvc;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description SpringMvc 自定义 JSON 参数解析器
 * @since 17
 */
public class JsonParamModelAttributeMethodProcessor implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 1、获取 http 请求参数
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 2、获取参数名
        JsonParam jsonParam = parameter.getParameterAnnotation(JsonParam.class);
        assert jsonParam != null;
        String paramName = jsonParam.value();
        // 3、注解没有给的参数名字，默认取参数名
        if (StringUtils.isBlank(paramName)) {
            paramName = parameter.getParameterName();
        }
        // 4、从 request 中拿到参数
        assert request != null;
        String json = request.getParameter(paramName);
        Class<?> parameterType = parameter.getParameterType();
        if (StringUtils.isNotBlank(json)) {
            return JSON.parseObject(json, parameterType);
        } else {
            return parameterType.getDeclaredConstructor().newInstance();
        }
    }
}