package com.lmyxlf.jian_mu.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class LogJacksonUtils {

    private static final ObjectMapper SNAKE_CASE_MAPPER = new ObjectMapper();

    static {
        SNAKE_CASE_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        SNAKE_CASE_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static String toJsonString(Object object) {
        try {
            return SNAKE_CASE_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException var3) {
            return "{json_error}";
        }
    }
}
