package com.lmyxlf.jian_mu.business.raising_numbers.util;

import com.lmyxlf.jian_mu.business.raising_numbers.constant.enums.PlatformTypeEnums;
import com.lmyxlf.jian_mu.business.raising_numbers.model.resp.RespUserAgent;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.util.LmyXlfHttp;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/25 18:47
 * @description 随机生成 User-Agent
 * @since 17
 */
@Slf4j
public class UserAgentUtil {

    /**
     * 随机生成 User-Agent 请求链接
     */
    private static final String GENERATE_URL = "https://www.bejson.com/Bejson/Api/Common/ge_nua";

    /**
     * 默认 User-Agent，windows 版
     */
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36";

    public static String generateRandomUA() {

        List<String> userAgent = generateRandomUA(1, PlatformTypeEnums.WINDOWS);

        return userAgent.get(0);
    }

    public static String generateRandomUA(PlatformTypeEnums type) {

        List<String> userAgent = generateRandomUA(1, type);

        return userAgent.get(0);
    }

    /**
     * count 正常情况下超过 30，最多请求 30 条，模拟请求情况下，不论 count 为多少，均返回 1 条
     *
     * @param count
     * @param type
     * @return
     */
    public static List<String> generateRandomUA(Integer count, PlatformTypeEnums type) {

        Map<String, String> params = new HashMap<>() {{
            put("count", String.valueOf(count));
            put("type", type.getName());
        }};

        RespUserAgent result = LmyXlfHttp
                .post(GENERATE_URL)
                .json(params)
                .build()
                .json(RespUserAgent.class);

        String code = result.getCode();
        if (CODE_MSG.SUCCESS.getCode().equals(code)) {
            List<String> data = result.getData();
            if (!data.isEmpty()) {
                log.info("随机生成 User-Agent：{}，params：{}，result：{}，", data, params, result);
                return data;
            }
        }

        List<String> data = Collections.singletonList(DEFAULT_USER_AGENT);
        log.info("使用默认 User-Agent：{}，params：{}，result：{}", data, params, result);
        return data;
    }
}