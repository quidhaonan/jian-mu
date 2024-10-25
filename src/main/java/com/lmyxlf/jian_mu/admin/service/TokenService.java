package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/26 0:57
 * @description
 * @since 17
 */
public interface TokenService {

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    LoginUserDTO getLoginUser(HttpServletRequest request);

    /**
     * 设置用户身份信息
     */
    void setLoginUser(LoginUserDTO loginUserDTO);

    /**
     * 删除用户身份信息
     */
    void delLoginUser(String token);

    /**
     * 创建令牌
     *
     * @param loginUserDTO 用户信息
     * @return 令牌
     */
    public String createToken(LoginUserDTO loginUserDTO);

    /**
     * 验证令牌有效期，相差不足 20 分钟，自动刷新缓存
     *
     * @param loginUserDTO
     * @return 令牌
     */
    public void verifyToken(LoginUserDTO loginUserDTO);

    /**
     * 刷新令牌有效期
     *
     * @param loginUserDTO 登录信息
     */
    public void refreshToken(LoginUserDTO loginUserDTO);

    /**
     * 设置用户代理信息
     *
     * @param loginUserDTO 登录信息
     */
    public void setUserAgent(LoginUserDTO loginUserDTO);


    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    String getUsernameFromToken(String token);
}