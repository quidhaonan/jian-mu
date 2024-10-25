package com.lmyxlf.jian_mu.admin.config;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson2.JSON;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.service.AsyncTaskService;
import com.lmyxlf.jian_mu.admin.service.TokenService;
import com.lmyxlf.jian_mu.global.handler.ThreadPoolsHandler;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.util.MessageUtil;
import com.lmyxlf.jian_mu.global.util.SpringMvcUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/26 0:55
 * @description
 * @since 17
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    private final TokenService tokenService;
    private final AsyncTaskService asyncTaskService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        LoginUserDTO loginUser = tokenService.getLoginUser(httpServletRequest);
        if (ObjUtil.isNotNull(loginUser)) {

            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                    userName, UserConstant.LOGOUT, MessageUtil.message("user.logout.success")));
        }

        SpringMvcUtil.renderString(httpServletResponse, JSON.toJSONString(LmyXlfResult.ok(MessageUtil.message("user.logout.success"))));
    }
}