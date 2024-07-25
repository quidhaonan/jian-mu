package com.lmyxlf.jian_mu.global.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 18:49
 * @description
 * @since 17
 */
@Slf4j
public class XiZhiNoticeUtil {

    // 单点推送接口地址
    private static final String DEFAULT_PUSH_URL = "https://xizhi.qqoq.net/XZd79d6a005769aa7591f2da559521d2a7.send";

    public static JSONObject xiZhiMsgNotice(String title, String content) {

        List<String> pushUrls = Collections.singletonList(DEFAULT_PUSH_URL);

        return xiZhiMsgNotice(pushUrls, title, content);
    }

    public static JSONObject xiZhiMsgNotice(List<String> pushUrls, String title, String content) {
        List<String> resultList = new ArrayList<>();

        Map<String, String> params = Map.ofEntries(
                Map.entry("title", title),
                Map.entry("content", content)
        );

        pushUrls.forEach(pushUrl -> {
            String result = LmyXlfHttp
                    .post(pushUrl)
                    .json(params)
                    .build()
                    .json(new TypeReference<>() {
                    });
            log.info("单点推送，pushUrl：{}，params：{}，result：{}", pushUrl, params, result);

            resultList.add(result);
        });

        return JSON.parseObject(resultList.toString());
    }
}