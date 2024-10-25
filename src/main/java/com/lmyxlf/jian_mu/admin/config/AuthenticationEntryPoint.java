package com.lmyxlf.jian_mu.admin.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.util.SpringMvcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/25 23:18
 * @description 认证失败处理类 返回未授权
 * @since 17
 */
@Slf4j
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        String code = CODE_MSG.UNAUTHORIZED.getCode();
        log.warn("请求访问：{}，认证失败，无法访问系统资源", httpServletRequest.getRequestURI());
        String msg = StrUtil.format("请求访问：{}，认证失败，无法访问系统资源", httpServletRequest.getRequestURI());
        SpringMvcUtil.renderString(httpServletResponse, JSON.toJSONString(LmyXlfResult.error(code, msg)));
    }
}