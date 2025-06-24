package com.lmyxlf.jian_mu.business.batch_invitation.common;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.business.batch_invitation.model.dto.YeZiGetMessageDto;
import com.lmyxlf.jian_mu.business.batch_invitation.model.dto.YeZiGetMobileDto;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespXingLiu;
import com.lmyxlf.jian_mu.business.batch_invitation.util.YeZiSMSUtil;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.util.LmyXlfHttp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/23 0:23
 * @description Async 在同方法调用不生效
 * @since 17
 */
@Slf4j
@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class BIAsyncMethods {

    /**
     * 星流 AI 椰子项目 id
     */
    private static final String XINGLIU_YEZI_PROJECT_ID = "718541";

    /**
     * cid 每个浏览器不同，同一个浏览器不会变
     */
    private static final String CID = "1750572036498euvlgsho";

    /**
     * 星流 AI 发送验证码 URL
     */
    private static final String SEND_LOGIN_PHONE_CODE = "https://passport.liblib.art/api/www/login/sendLoginPhoneCode";

    /**
     * 星流 AI 验证码登录 URL
     */
    private static final String LOGIN_BY_PHONE_CODE = "https://passport.liblib.art/api/www/login/loginByPhoneCode";

    /**
     * 星流 AI 绑定邀请码 URL
     */
    private static final String LOGIN_USER_EVENT_REPORT = "https://api2.xingliu.art/api/www/login/user/event/report";

    /**
     * 星流 AI 正常返回状态码
     */
    private static final Integer SUCCESS_CODE = 0;

    /**
     * 星流 AI 批量邀请
     *
     * @param inviteCode
     */
    @Async("async_executor_bi")
    public void xingLiuInviteBatch(String inviteCode) {

        log.info("星流 AI 批量邀请，inviteCode：{}", inviteCode);
        // 获得手机号
        YeZiGetMobileDto yeZiGetMobileDto = YeZiSMSUtil.getMobile(YeZiSMSUtil.YEZI_TOKEN, XINGLIU_YEZI_PROJECT_ID);
        String mobile = yeZiGetMobileDto.getMobile();

        // 发送验证码
        Map<String, Object> sendLoginPhoneCodeParams = new HashMap<>() {{

            put("phone", mobile);
            put("platform", "xingliu");
        }};
        RespXingLiu respSendLoginPhoneCode = LmyXlfHttp
                .post(SEND_LOGIN_PHONE_CODE)
                .json(sendLoginPhoneCodeParams)
                .build()
                .json(RespXingLiu.class);
        if (!SUCCESS_CODE.equals(respSendLoginPhoneCode.getCode())) {

            log.error("星流 AI 发送验证码失败，inviteCode：{}，respXingLiu：{}", inviteCode, respSendLoginPhoneCode);
            return;
        }
        log.info("星流 AI 发送验证码成功，inviteCode：{}，respXingLiu：{}", inviteCode, respSendLoginPhoneCode);

        // 接收验证码
        // 循环获取验证码次数
        int i = 36;
        // 验证码
        String code = null;
        while (i-- > 0) {

            YeZiGetMessageDto yeZiGetMessageDto = YeZiSMSUtil.getMessage(
                    YeZiSMSUtil.YEZI_TOKEN, XINGLIU_YEZI_PROJECT_ID, null, mobile);
            if (ObjUtil.isNotNull(yeZiGetMessageDto)) {

                code = yeZiGetMessageDto.getCode();
                break;
            }
            try {

                log.info("正在接收椰子验证码，线程名：{}，inviteCode：{}，yeZiGetMessageDto：{}",
                        Thread.currentThread().getName(), inviteCode, yeZiGetMessageDto);
                Thread.sleep(5000);
            } catch (InterruptedException e) {

                log.error("椰子获取验证码休眠失败，inviteCode：{}", inviteCode);
            }
        }

        // 获取星流 AI token
        if (StrUtil.isEmpty(code)) {

            log.error("椰子获取验证码失败，inviteCode：{}，yeZiGetMobileDto：{}", inviteCode, yeZiGetMobileDto);
            // 释放号码
            YeZiSMSUtil.freeMobile(YeZiSMSUtil.YEZI_TOKEN, XINGLIU_YEZI_PROJECT_ID, null, mobile);
            return;
        }
        Map<String, Object> loginByPhoneCodeParams = new HashMap<>() {{

            put("phone", mobile);
            put("cid", CID);
            put("platform", "xingliu");
        }};
        loginByPhoneCodeParams.put("code", code);
        RespXingLiu respLoginByPhoneCode = LmyXlfHttp
                .post(LOGIN_BY_PHONE_CODE)
                .json(loginByPhoneCodeParams)
                .build()
                .json(RespXingLiu.class);
        if (!SUCCESS_CODE.equals(respLoginByPhoneCode.getCode())) {

            log.error("星流 AI 登录失败，inviteCode：{}，yeZiGetMobileDto：{}，respLoginByPhoneCode：{}",
                    inviteCode, yeZiGetMobileDto, respLoginByPhoneCode);
            return;
        }
        log.info("星流 AI 登录成功，inviteCode：{}，yeZiGetMobileDto：{}，respLoginByPhoneCode：{}",
                inviteCode, yeZiGetMobileDto, respLoginByPhoneCode);

        // 绑定邀请码
        String token = respLoginByPhoneCode.getData().getToken();
        Map<String, Object> loginUserEventReportParams = new HashMap<>() {{

            put("libId", 1);
            put("inviteCode", inviteCode);
            put("cid", CID);
        }};
        RespXingLiu respLoginUserEventReport = LmyXlfHttp
                .post(LOGIN_USER_EVENT_REPORT)
                .header(LmyXlfReqParamConstant.TOKEN, token)
                .json(loginUserEventReportParams)
                .build()
                .json(RespXingLiu.class);
        log.info("星流 AI 绑定邀请码，respLoginUserEventReport：{}", respLoginUserEventReport);
        // 拉黑号码
        YeZiSMSUtil.addBlacklist(YeZiSMSUtil.YEZI_TOKEN, XINGLIU_YEZI_PROJECT_ID, null, mobile);
    }
}