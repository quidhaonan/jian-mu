package com.lmyxlf.jian_mu.business.raising_numbers.scheduled;

import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.RNConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.RNCornConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import com.lmyxlf.jian_mu.business.raising_numbers.model.enums.RaisingNumbersTypeEnums;
import com.lmyxlf.jian_mu.business.raising_numbers.model.resp.RespJingyiForum;
import com.lmyxlf.jian_mu.business.raising_numbers.service.RaisingNumbersService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.util.LmyXlfHttp;
import com.lmyxlf.jian_mu.global.util.XiZhiNoticeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
 * @date 2024/7/24 22:29
 * @description 精易论坛
 * @since 17
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JingyiForumScheduled {

    private final RaisingNumbersService raisingNumbersService;

    /**
     * 签到 url
     */
    private final static String SIGN_IN_URL = "https://bbs.125.la/plugin.php?" +
            "id=dsu_paulsign:sign&operation=qiandao&infloat=1&formhash=ad509b0f&submit=1&targerurl=&todaysay=&qdxq=kx";

    /**
     * 签到成功码
     */
    private final static String CODE_SUCCESS = "0";

    @Async("async_executor_rn")
    @Scheduled(cron = RNCornConstant.EVERY_DAY_12_CLOCK_0_MINUTE_PM)
    public void signIn() {
        log.info("开始[{}]养号", RaisingNumbersTypeEnums.JINGYI_FORUM.getName());
        // 养号失败列表
        List<RespJingyiForum> failList = new ArrayList<>();

        // 请求 cookie
        List<RaisingNumbers> raisingNumbersList = raisingNumbersService.lambdaQuery()
                .eq(RaisingNumbers::getType, RaisingNumbersTypeEnums.JINGYI_FORUM.getType())
                .list();

        // 养号
        raisingNumbersList.forEach(item -> {
            // 请求头
            Map<String, String> headers = getHeaders(item.getToken());
            RespJingyiForum result = LmyXlfHttp.post(SIGN_IN_URL)
                    .header(headers)
                    .build()
                    .json(RespJingyiForum.class);
            log.info("[{}]养号，item：{}，result：{}", RaisingNumbersTypeEnums.JINGYI_FORUM.getName(), item, result);

            String status = result.getStatus();
            if (!CODE_SUCCESS.equals(status)) {
                result.setRaisingNumbers(item);
                failList.add(result);
            }

            // 随机休眠
            int nextInt = ThreadLocalRandom.current().nextInt(RNConstant.SLEEP_DURATION);
            try {
                Thread.sleep(nextInt);
            } catch (InterruptedException e) {
                log.error("休眠失败，nextInt：{}", nextInt);
            }
        });

        if (!failList.isEmpty()) {
            log.error("[{}]养号失败，failList：{}", RaisingNumbersTypeEnums.JINGYI_FORUM.getName(), failList);
            XiZhiNoticeUtil.xiZhiMsgNotice(RaisingNumbersTypeEnums.JINGYI_FORUM.getName(), JSONUtil.toJsonStr(LmyXlfResult.ok(failList)));
        }
    }

    /**
     * 获得请求头
     *
     * @return
     */
    private Map<String, String> getHeaders(String cookie) {
        return new HashMap<>() {{
            put("Host", "bbs.125.la");
            put("Connection", "keep-alive");
            put("sec-ch-ua", "\";Not A Brand\";v=\"99\", \"Chromium\";v=\"94\"");
            put("Accept", "*/*");
            put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            put("X-Requested-With", "XMLHttpRequest");
            put("sec-ch-ua-mobile", "?0");
            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Core/1.94.263.400 QQBrowser/12.7.5748.400");
            put("sec-ch-ua-platform", "\"Windows\"");
            put("Origin", "https://bbs.125.la");
            put("Sec-Fetch-Site", "same-origin");
            put("Sec-Fetch-Mode", "cors");
            put("Sec-Fetch-Dest", "empty");
            put("Referer", "https://bbs.125.la/dsu_paulsign-sign.html");
            put("Accept-Encoding", "gzip, deflate, br");
            put("Accept-Language", "zh-CN,zh;q=0.9");
            put("Cookie", cookie);
        }};
    }
}