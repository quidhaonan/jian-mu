package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.handler.AuthenticationContextHolder;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.model.req.ReqLoginBody;
import com.lmyxlf.jian_mu.admin.model.resp.RespLoginUserInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespRouter;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysMenu;
import com.lmyxlf.jian_mu.admin.service.*;
import com.lmyxlf.jian_mu.admin.util.IPUtil;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.handler.ThreadPoolsHandler;
import com.lmyxlf.jian_mu.global.util.IPUtils;
import com.lmyxlf.jian_mu.global.util.MessageUtil;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import com.lmyxlf.jian_mu.global.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/22 1:02
 * @description
 * @since 17
 */
@Slf4j
@Service("sysLoginService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysLoginServiceImpl implements SysLoginService {

    private final SysConfigService sysConfigService;
    private final AsyncTaskService asyncTaskService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final SysPermissionService sysPermissionService;
    private final SysMenuService sysMenuService;

    @Override
    public String login(ReqLoginBody reqLoginBody) {

        String username = reqLoginBody.getUsername();
        String password = reqLoginBody.getPassword();
        String code = reqLoginBody.getCode();
        String uuid = reqLoginBody.getUuid();

        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {

            if (e instanceof BadCredentialsException) {

                ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                        username, UserConstant.LOGIN_FAIL, MessageUtil.message("user.password.not.match")));
                throw new LmyXlfException("user.password.not.match");
            } else {

                ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                        username, UserConstant.LOGIN_FAIL, e.getMessage()));
                throw new LmyXlfException(e.getMessage());
            }
        } finally {

            AuthenticationContextHolder.clearContext();
        }

        ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(username, UserConstant.LOGIN_SUCCESS, MessageUtil.message("user.login.success")));
        LoginUserDTO loginUserDTO = (LoginUserDTO) authentication.getPrincipal();
        recordLoginInfo(loginUserDTO.getId());
        // 生成 token
        return tokenService.createToken(loginUserDTO);
    }

    @Override
    public RespLoginUserInfo getInfo() {

        SysUserDTO sysUserDTO = SecurityUtil.getLoginUser().getSysUserDTO();
        // 角色集合
        Set<String> roles = sysPermissionService.getRolePermission(sysUserDTO.getId());
        // 权限集合
        Set<String> permissions = sysPermissionService.getMenuPermission(sysUserDTO);

        RespLoginUserInfo respLoginUserInfo = new RespLoginUserInfo();
        respLoginUserInfo.setSysUserDTO(sysUserDTO)
                .setRoles(roles)
                .setPermissions(permissions);
        return respLoginUserInfo;
    }

    @Override
    public List<RespRouter> getRouters() {

        Integer userId = SecurityUtil.getUserId();

        List<RespSysMenu> respSysMenuList = sysMenuService.selectMenuTreeByUserId(userId);

        return sysMenuService.buildMenus(respSysMenuList);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {

        boolean captchaEnabled = sysConfigService.selectCaptchaEnabled();
        if (captchaEnabled) {

            String verifyKey = CacheConstant.CAPTCHA_CODE_KEY + (StrUtil.isNotEmpty(uuid) ? uuid : "");
            String captcha = RedisUtil.get(verifyKey);
            if (ObjUtil.isNull(captcha)) {

                ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                        username, UserConstant.LOGIN_FAIL, MessageUtil.message("user.jcaptcha.expire")));
                throw new LmyXlfException("user.jcaptcha.expire");
            }
            RedisUtil.delete(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {

                ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                        username, UserConstant.LOGIN_FAIL, MessageUtil.message("user.jcaptcha.error")));
                throw new LmyXlfException("user.jcaptcha.error");
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {

        // 用户名或密码为空 错误
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {

            ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                    username, UserConstant.LOGIN_FAIL, MessageUtil.message("not.null")));
            throw new LmyXlfException("user.not.exists");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstant.PASSWORD_MIN_LENGTH
                || password.length() > UserConstant.PASSWORD_MAX_LENGTH) {

            ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                    username, UserConstant.LOGIN_FAIL, MessageUtil.message("user.password.not.match")));
            throw new LmyXlfException("user.password.not.match");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstant.USERNAME_MIN_LENGTH
                || username.length() > UserConstant.USERNAME_MAX_LENGTH) {

            ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                    username, UserConstant.LOGIN_FAIL, MessageUtil.message("user.password.not.match")));
            throw new LmyXlfException("user.password.not.match");
        }
        // ip 黑名单校验
        String blackStr = sysConfigService.selectConfigByKey("sys.login.blackIPList");
        if (IPUtil.isMatchedIp(blackStr, IPUtils.getIpAddr())) {

            ThreadPoolsHandler.ASYNC_SCHEDULED_POOL.execute(asyncTaskService.recordLogininfor(
                    username, UserConstant.LOGIN_FAIL, MessageUtil.message("login.blocked")));
            throw new LmyXlfException("login.blocked");
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户 id
     */
    public void recordLoginInfo(Integer userId) {

        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setLoginIp(IPUtils.getIpAddr());
        sysUser.setLastLoginTime(LocalDateTimeUtil.now());
        SpringContextHolder.getBean(SysUserService.class).updateById(sysUser);
    }
}