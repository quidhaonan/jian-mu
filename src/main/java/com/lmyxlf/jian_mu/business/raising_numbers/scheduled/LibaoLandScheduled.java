package com.lmyxlf.jian_mu.business.raising_numbers.scheduled;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.RNCornConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import com.lmyxlf.jian_mu.business.raising_numbers.model.enums.RaisingNumbersTypeEnums;
import com.lmyxlf.jian_mu.business.raising_numbers.model.req.ReqLibaoLand;
import com.lmyxlf.jian_mu.business.raising_numbers.model.resp.RespLibaoLand;
import com.lmyxlf.jian_mu.business.raising_numbers.service.RaisingNumbersService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.util.LmyXlfHttp;
import com.lmyxlf.jian_mu.global.util.XiZhiNoticeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/24 0:08
 * @description 丽宝乐园
 * @since 17
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LibaoLandScheduled {

    private final RaisingNumbersService raisingNumbersService;

    /**
     * 签到 url
     */
    private final static String SIGN_IN_URL = "https://m.mallcoo.cn/api/user/User/CheckinV2";

    /**
     * 签到成功码
     */
    private final static String CODE_SUCCESS = "1";

    /**
     * 今日已签到成功码
     */
    private final static String CODE_REPEAT = "2054";

    /**
     * 签到
     */
    @Scheduled(cron = RNCornConstant.EVERY_DAY_0_CLOCK_1_MINUTE_AM)
    public void signIn() {
        log.info("开始{}养号,时间：{}", RaisingNumbersTypeEnums.LIBAO_LAND.getName(), DateUtil.now());
        // 养号失败列表
        List<RespLibaoLand> failList = new ArrayList<>();

        // 请求头
        Map<String, String> headers = getHeaders();

        // 请求 token
        List<RaisingNumbers> raisingNumbersList = raisingNumbersService.lambdaQuery()
                .eq(RaisingNumbers::getType, RaisingNumbersTypeEnums.LIBAO_LAND.getType())
                .list();
        // 养号
        raisingNumbersList.forEach(item -> {
            // 请求参数
            ReqLibaoLand params = new ReqLibaoLand(item.getToken());
            RespLibaoLand result = LmyXlfHttp
                    .post(SIGN_IN_URL)
                    .header(headers)
                    .json(params)
                    .proxy()
                    .build().json(RespLibaoLand.class);
            log.info("{}养号，item：{}，result：{}", RaisingNumbersTypeEnums.LIBAO_LAND.getName(), item, result);
            String m = result.getM();
            if (!CODE_SUCCESS.equals(m) && !CODE_REPEAT.equals(m)) {
                result.setRaisingNumbers(item);
                failList.add(result);
            }

            // 随机休眠
            int nextInt = ThreadLocalRandom.current().nextInt(9999);
            try {
                Thread.sleep(nextInt);
            } catch (InterruptedException e) {
                log.error("休眠失败，nextInt：{}", nextInt);
            }
        });

        if (!failList.isEmpty()) {
            log.error("{}养号失败，failList：{}", RaisingNumbersTypeEnums.LIBAO_LAND.getName(), failList);
            XiZhiNoticeUtil.xiZhiMsgNotice(RaisingNumbersTypeEnums.LIBAO_LAND.getName(), JSONUtil.toJsonStr(LmyXlfResult.ok(failList)));
        }
    }

    /**
     * 获得请求头
     * @return
     */
    public Map<String, String> getHeaders() {
        return new HashMap<>() {{
            put("Host", "m.mallcoo.cn");
            put("Connection", "keep-alive");
            put("xweb_xhr", "1");
            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF WindowsWechat(0x63090819)XWEB/11097");
            put("Content-Type", "application/json");
            put("Accept", "*/*");
            put("Sec-Fetch-Site", "cross-site");
            put("Sec-Fetch-Mode", "cors");
            put("Sec-Fetch-Dest", "empty");
            put("Referer", "https://servicewechat.com/wx73572d1e4a9dfbf6/23/page-frame.html");
            put("Accept-Encoding", "gzip, deflate, br");
            put("Accept-Language", "zh-CN,zh;q=0.9");
        }};
    }
}