package com.lmyxlf.jian_mu.admin.service.impl;

import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.handler.AuthenticationContextHolder;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.service.SysPasswordService;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 2:27
 * @description
 * @since 17
 */
@Slf4j
@Service("sysPasswordService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysPasswordServiceImpl implements SysPasswordService {

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {

        return CacheConstant.PWD_ERR_CNT_KEY + username;
    }

    @Override
    public void validate(SysUser user) {

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = RedisUtil.get(getCacheKey(username));

        if (retryCount == null) {

            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {

            throw new LmyXlfException("验证码输入次数过多");
        }

        if (!matches(user, password)) {

            retryCount = retryCount + 1;
            RedisUtil.set(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new LmyXlfException("账号或密码错误");
        } else {

            clearLoginRecordCache(username);
        }
    }

    @Override
    public boolean matches(SysUser user, String rawPassword) {

        return SecurityUtil.matchesPassword(rawPassword, user.getPassword());
    }

    @Override
    public void clearLoginRecordCache(String loginName) {

        if (RedisUtil.hasKey(getCacheKey(loginName))) {

            RedisUtil.delete(getCacheKey(loginName));
        }
    }
}