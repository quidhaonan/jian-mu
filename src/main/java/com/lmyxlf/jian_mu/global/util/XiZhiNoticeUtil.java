package com.lmyxlf.jian_mu.global.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 18:49
 * @description
 * @since 17
 */
public class XiZhiNoticeUtil {

    // 单点推送接口地址
    private static final String pushUrl = "https://xizhi.qqoq.net/XZd79d6a005769aa7591f2da559521d2a7.send";

    public static JSONObject xiZhiMsgNotice(String title, String content) {

        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);

        Object json = LmyXlfHttp.builder()
                .url(pushUrl)
                .json(params)
                .post()
                .build().json(new TypeReference<>() {
                });
        return JSON.parseObject(json.toString());
    }
}