package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.model.HttpResult;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 18:49
 * @description
 * @since 17
 */
@Slf4j
public class XiZhiNoticeUtil {

    // 默认推送内容
    private static final String DEFAULT_CONENT = "此为默认推送内容";

    // 单点推送接口地址
    private static final String DEFAULT_PUSH_URL = "https://xizhi.qqoq.net/XZd79d6a005769aa7591f2da559521d2a7.send";

    public static JSONArray xiZhiMsgNotice(String title, Object content) {

        List<String> pushUrls = Collections.singletonList(DEFAULT_PUSH_URL);

        return xiZhiMsgNotice(pushUrls, title, content);
    }

    public static JSONArray xiZhiMsgNotice(List<String> pushUrls, String title, Object content) {

        if (CollUtil.isEmpty(pushUrls)) {
            return xiZhiMsgNotice(title, content);
        }
        List<String> resultList = new ArrayList<>();

        Map<String, String> params = new HashMap<>() {{
            put("title", title);
            put("content", JSONUtil.toJsonStr(content));
        }};

        pushUrls.forEach(pushUrl -> {
            HttpResult exchange = LmyXlfHttp
                    .post(pushUrl)
                    .json(params)
                    .build().exchange();
            String result = exchange.getResult();

            if (!CODE_MSG.SUCCESS.getCode().equals(String.valueOf(exchange.getStatusCode()))
                    && !DEFAULT_CONENT.equals(content)) {
                // 存在 content 是整个网页，发送失败的情况
                result = xiZhiMsgNotice(Collections.singletonList(pushUrl), title, DEFAULT_CONENT).toString();
            }
            log.info("单点推送，pushUrl：{}，params：{}，result：{}", pushUrl, params, result);

            resultList.add(result);
        });

        return JSONUtil.parseArray(resultList);
    }
}