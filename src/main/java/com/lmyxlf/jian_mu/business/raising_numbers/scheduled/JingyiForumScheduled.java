package com.lmyxlf.jian_mu.business.raising_numbers.scheduled;

import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.ProjectTypeConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.constant.RNConstant;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.RaisingNumbers;
import com.lmyxlf.jian_mu.business.raising_numbers.model.enums.ProjectTypeEnums;
import com.lmyxlf.jian_mu.business.raising_numbers.model.resp.RespJingyiForum;
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
    private static final String DAILY_CHECK_IN_URL = "https://bbs.125.la/plugin.php?" +
            "id=dsu_paulsign:sign&operation=qiandao&infloat=1&formhash=ad509b0f&submit=1&targerurl=&todaysay=&qdxq=kx";

    /**
     * 签到成功码
     */
    private static final String CODE_SUCCESS = "1";

    /**
     * 已签到成功码
     */
    private static final String CODE_REPEAT = "0";

    @Async("async_executor_rn")
    @XxlJob("jingyiForumHandler")
    // @Scheduled(cron = RNCornConstant.EVERY_DAY_12_CLOCK_0_MINUTE_PM)
    @NoticeErrorAnnotation(title = ProjectTypeConstant.JINGYI_FORUM + ProjectTypeConstant.UN_KNOWN_ERROR, filter = {ProjectTypeConstant.JINGYI_FORUM})
    public void dailyCheckIn() {
        String projectName = ProjectTypeEnums.JINGYI_FORUM.getName();
        log.info("开始[{}]养号", projectName);
        // 养号失败列表
        List<RespJingyiForum> failedList = new ArrayList<>();

        // 请求 cookie
        List<RaisingNumbers> raisingNumbersList = raisingNumbersService.lambdaQuery()
                .eq(RaisingNumbers::getProjectId, ProjectTypeEnums.JINGYI_FORUM.getId())
                .eq(RaisingNumbers::getIsDelete, DBConstant.NOT_DELETED)
                .list();

        // 养号
        raisingNumbersList.forEach(item -> {
            // 请求头
            Map<String, String> headers = getHeaders(item.getToken());
            RespJingyiForum result = LmyXlfHttp.post(DAILY_CHECK_IN_URL)
                    .header(headers)
                    .build()
                    .json(RespJingyiForum.class);
            log.info("[{}]养号，item：{}，result：{}", projectName, item, result);

            String status = result.getStatus();
            if (!CODE_SUCCESS.equals(status) && !CODE_REPEAT.equals(status)) {
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
    private Map<String, String> getHeaders(String cookie) {
        return new HashMap<>() {{
            put("Host", "bbs.125.la");
            put("Connection", "keep-alive");
            put("sec-ch-ua", "\";Not A Brand\";v=\"99\", \"Chromium\";v=\"94\"");
            put("Accept", "*/*");
            put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            put("X-Requested-With", "XMLHttpRequest");
            put("sec-ch-ua-mobile", "?0");
            put("User-Agent", UserAgentUtil.generateRandomUA());
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