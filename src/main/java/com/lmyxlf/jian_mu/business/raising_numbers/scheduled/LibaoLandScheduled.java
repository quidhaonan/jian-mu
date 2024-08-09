package com.lmyxlf.jian_mu.business.raising_numbers.scheduled;

import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.ProjectTypeConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.RNConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import com.lmyxlf.jian_mu.business.raising_numbers.model.enums.ProjectTypeEnums;
import com.lmyxlf.jian_mu.business.raising_numbers.model.req.ReqLibaoLand;
import com.lmyxlf.jian_mu.business.raising_numbers.model.resp.RespLibaoLand;
import com.lmyxlf.jian_mu.business.raising_numbers.service.RaisingNumbersService;
import com.lmyxlf.jian_mu.business.raising_numbers.util.RandomHibernationUtil;
import com.lmyxlf.jian_mu.business.raising_numbers.util.UserAgentUtil;
import com.lmyxlf.jian_mu.global.annotation.NoticeErrorAnnotation;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.util.LmyXlfHttp;
import com.lmyxlf.jian_mu.global.util.XiZhiNoticeUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String DAILY_CHECK_IN_URL = "https://m.mallcoo.cn/api/user/User/CheckinV2";

    /**
     * 签到成功码
     */
    private static final String CODE_SUCCESS = "1";

    /**
     * 已签到成功码
     */
    private static final String CODE_REPEAT = "2054";

    /**
     * 签到
     */
    @Async("async_executor_rn")
    @XxlJob("libaoLandHandler")
    // @Scheduled(cron = RNCornConstant.EVERY_DAY_0_CLOCK_1_MINUTE_AM)
    @NoticeErrorAnnotation(title = ProjectTypeConstant.LIBAO_LAND + ProjectTypeConstant.UN_KNOWN_ERROR, filter = {ProjectTypeConstant.LIBAO_LAND})
    public void dailyCheckIn() {
        String projectName = ProjectTypeEnums.LIBAO_LAND.getName();
        log.info("开始[{}]养号", projectName);
        // 养号失败列表
        List<RespLibaoLand> failedList = new ArrayList<>();

        // 请求头
        Map<String, String> headers = getHeaders();

        // 请求 token
        List<RaisingNumbers> raisingNumbersList = raisingNumbersService.lambdaQuery()
                .eq(RaisingNumbers::getProjectId, ProjectTypeEnums.LIBAO_LAND.getId())
                .eq(RaisingNumbers::getIsDelete, DBConstant.NOT_DELETED)
                .list();
        // 养号
        raisingNumbersList.forEach(item -> {
            // 请求参数
            ReqLibaoLand params = new ReqLibaoLand(item.getToken());
            RespLibaoLand result = LmyXlfHttp
                    .post(DAILY_CHECK_IN_URL)
                    .header(headers)
                    .json(params)
                    .proxy()
                    .build()
                    .json(RespLibaoLand.class);
            log.info("[{}]养号，item：{}，result：{}", projectName, item, result);

            String m = result.getM();
            if (!CODE_SUCCESS.equals(m) && !CODE_REPEAT.equals(m)) {
                result.setRaisingNumbers(item);
                failedList.add(result);
            }

            // 随机休眠
            RandomHibernationUtil.randomHibernation();
        });

        String title = failedList.isEmpty() ? projectName + RNConstant.RN_SUCCEED : projectName + RNConstant.RN_FAILED;
        XiZhiNoticeUtil.xiZhiMsgNotice(title, JSONUtil.toJsonStr(LmyXlfResult.ok(failedList)));
    }

    /**
     * 获得请求头
     *
     * @return
     */
    private Map<String, String> getHeaders() {
        return new HashMap<>() {{
            put("Host", "m.mallcoo.cn");
            put("Connection", "keep-alive");
            put("xweb_xhr", "1");
            put("User-Agent", UserAgentUtil.generateRandomUA());
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