package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.model.req.ReqLoginBody;
import com.lmyxlf.jian_mu.admin.service.AsyncTaskService;
import com.lmyxlf.jian_mu.admin.service.SysConfigService;
import com.lmyxlf.jian_mu.admin.service.SysRegisterService;
import com.lmyxlf.jian_mu.admin.service.SysUserService;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.handler.ThreadPoolsHandler;
import com.lmyxlf.jian_mu.global.util.MessageUtil;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 13:18
 * @description
 * @since 17
 */
@Slf4j
@Service("sysRegisterService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRegisterServiceImpl implements SysRegisterService {

    private static final String SYS_ACCOUNT_REGISTERUSER = "sys.account.registerUser";

    private final SysConfigService sysConfigService;
    private final AsyncTaskService asyncTaskService;
    private final SysUserService sysUserService;

    @Override
    public Boolean register(ReqLoginBody reqLoginBody) {

        String username = reqLoginBody.getUsername();
        String password = reqLoginBody.getPassword();

        if (!(Boolean.TRUE.toString().equals(sysConfigService.selectConfigByKey(SYS_ACCOUNT_REGISTERUSER)))) {

            log.warn("当前系统没有开启注册功能reqLoginBody：{}", reqLoginBody);
            throw new LmyXlfException("当前系统没有开启注册功能");
        }

        // 验证码开关
        boolean captchaEnabled = sysConfigService.selectCaptchaEnabled();
        if (captchaEnabled) {

            validateCaptcha(reqLoginBody.getCode(), reqLoginBody.getUuid());
        }

        SysUser sysUser = new SysUser();
        sysUser
                .setUserName(username)
                .setNickName(username)
                .setPassword(SecurityUtil.encryptPassword(password));
        boolean save = sysUserService.save(sysUser);
        if (save) {

            ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                    username, UserConstant.REGISTER, MessageUtil.message("user.register.success")));

            return Boolean.TRUE;
        }

        throw new LmyXlfException("注册失败,请联系系统管理人员");
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    private void validateCaptcha(String code, String uuid) {

        String verifyKey = CacheConstant.CAPTCHA_CODE_KEY + (ObjUtil.isNotNull(uuid) ? uuid : "");
        String captcha = RedisUtil.get(verifyKey);

        RedisUtil.delete(verifyKey);

        if (StrUtil.isEmpty(captcha)) {

            log.warn("验证码过期，code：{}，uuid：{}", code, uuid);
            throw new LmyXlfException("user.jcaptcha.expire");
        }
        if (!code.equalsIgnoreCase(captcha)) {

            log.warn("验证码错误，code：{}，uuid：{}", code, uuid);
            throw new LmyXlfException("user.jcaptcha.error");
        }
    }
}