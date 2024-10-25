package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.model.entity.SysLoginInfo;
import com.lmyxlf.jian_mu.admin.model.entity.SysOperLog;
import com.lmyxlf.jian_mu.admin.service.AsyncTaskService;
import com.lmyxlf.jian_mu.admin.service.SysLoginInfoService;
import com.lmyxlf.jian_mu.admin.service.SysOperLogService;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.util.IPUtils;
import com.lmyxlf.jian_mu.global.util.SpringMvcUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/22 2:19
 * @description
 * @since 17
 */
@Slf4j
@Service("asyncTaskService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private final SysLoginInfoService sysLoginInfoService;
    private final SysOperLogService sysOperLogService;

    @Override
    public TimerTask recordLogininfor(String username, String status, String message, Object... args) {

        final UserAgent userAgent = UserAgent.parseUserAgentString(
                SpringMvcUtil.getRequest().orElseThrow().getHeader(LmyXlfReqParamConstant.USER_AGENT));
        final String ip = IPUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {

                String address = IPUtils.getAddressByIp(ip);
                String s = getBlock(ip) +
                        address +
                        getBlock(username) +
                        getBlock(status) +
                        getBlock(message);
                // 打印信息到日志
                log.info(s, args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginInfo sysLoginInfo = new SysLoginInfo();
                sysLoginInfo.setUserName(username);
                sysLoginInfo.setLoginIp(ip);
                sysLoginInfo.setLoginLocation(address);
                sysLoginInfo.setBrowserType(browser);
                sysLoginInfo.setOs(os);
                sysLoginInfo.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, UserConstant.LOGIN_SUCCESS, UserConstant.LOGOUT, UserConstant.REGISTER)) {

                    sysLoginInfo.setStatus(UserConstant.NORMAL);
                } else if (UserConstant.LOGIN_FAIL.equals(status)) {

                    sysLoginInfo.setStatus(UserConstant.EXCEPTION);
                }
                // 插入数据
                sysLoginInfoService.save(sysLoginInfo);
            }
        };
    }

    @Override
    public TimerTask recordOper(SysOperLog sysOperLog) {

        return new TimerTask() {
            @Override
            public void run() {

                // 远程查询操作地点
                sysOperLog.setOperLocation(IPUtils.getAddressByIp(sysOperLog.getOperIp()));
                sysOperLogService.save(sysOperLog);
            }
        };
    }

    private static String getBlock(Object msg) {

        if (ObjUtil.isNull(msg)) {

            msg = "";
        }

        return "[" + msg + "]";
    }
}