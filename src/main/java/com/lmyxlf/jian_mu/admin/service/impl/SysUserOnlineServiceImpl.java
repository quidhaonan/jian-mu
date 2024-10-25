package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserOnlineDTO;
import com.lmyxlf.jian_mu.admin.service.SysUserOnlineService;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/15 1:06
 * @description
 * @since 17
 */
@Slf4j
@Service("sysUserOnlineService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysUserOnlineServiceImpl implements SysUserOnlineService {

    @Override
    public List<SysUserOnlineDTO> list(String ipaddr, String userName) {

        Collection<String> keys = RedisUtil.keys(CacheConstant.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnlineDTO> userOnlineList = new ArrayList<>();
        for (String key : keys) {

            LoginUserDTO user = RedisUtil.get(key);

            if (StrUtil.isNotEmpty(ipaddr) && StrUtil.isNotEmpty(userName)) {

                userOnlineList.add(selectOnlineByInfo(ipaddr, userName, user));
            } else if (StrUtil.isNotEmpty(ipaddr)) {

                userOnlineList.add(selectOnlineByIpaddr(ipaddr, user));
            } else if (StrUtil.isNotEmpty(userName) && ObjUtil.isNotNull(user.getSysUserDTO())) {

                userOnlineList.add(selectOnlineByUserName(userName, user));
            } else {

                userOnlineList.add(loginUserToUserOnline(user));
            }
        }

        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));

        return userOnlineList;
    }

    @Override
    public Boolean forceLogout(String tokenId) {

        return RedisUtil.delete(CacheConstant.LOGIN_TOKEN_KEY + tokenId);
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    public SysUserOnlineDTO selectOnlineByInfo(String ipaddr, String userName, LoginUserDTO user) {

        if (StrUtil.equals(ipaddr, user.getLoginIp()) && StrUtil.equals(userName, user.getUsername())) {

            return loginUserToUserOnline(user);
        }

        return null;
    }

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    public SysUserOnlineDTO selectOnlineByIpaddr(String ipaddr, LoginUserDTO user) {

        if (StrUtil.equals(ipaddr, user.getLoginIp())) {

            return loginUserToUserOnline(user);
        }

        return null;
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    public SysUserOnlineDTO selectOnlineByUserName(String userName, LoginUserDTO user) {

        if (StrUtil.equals(userName, user.getUsername())) {

            return loginUserToUserOnline(user);
        }

        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    public SysUserOnlineDTO loginUserToUserOnline(LoginUserDTO user) {

        if (ObjUtil.isNull(user) || ObjUtil.isNull(user.getSysUserDTO())) {

            return null;
        }
        SysUserOnlineDTO sysUserOnlineDTO = new SysUserOnlineDTO();
        sysUserOnlineDTO.setTokenId(user.getToken());
        sysUserOnlineDTO.setUserName(user.getUsername());
        sysUserOnlineDTO.setIpaddr(user.getLoginIp());
        sysUserOnlineDTO.setLoginLocation(user.getLoginLocation());
        sysUserOnlineDTO.setBrowser(user.getBrowserType());
        sysUserOnlineDTO.setOs(user.getOs());
        sysUserOnlineDTO.setLoginTime(user.getLoginTime());
        if (ObjUtil.isNotNull(user.getSysUserDTO().getSysDeptDTO())) {

            sysUserOnlineDTO.setDeptName(user.getSysUserDTO().getSysDeptDTO().getDeptName());
        }

        return sysUserOnlineDTO;
    }
}