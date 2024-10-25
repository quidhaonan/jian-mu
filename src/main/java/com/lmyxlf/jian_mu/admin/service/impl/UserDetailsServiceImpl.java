package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.service.SysPasswordService;
import com.lmyxlf.jian_mu.admin.service.SysPermissionService;
import com.lmyxlf.jian_mu.admin.service.SysUserService;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/25 1:03
 * @description
 * @since 17
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;
    private final SysPasswordService sysPasswordService;
    private final SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.lambdaQuery()
                .eq(SysUser::getUserName, username)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(sysUser)) {

            log.info("登录用户：{} 不存在.", username);
            throw new LmyXlfException(MessageUtil.message("user.not.exists"));
        }

        sysPasswordService.validate(sysUser);

        return createLoginUser(sysUser);
    }

    public UserDetails createLoginUser(SysUser user) {

        SysUserDTO sysUserDTO = new SysUserDTO();
        BeanUtils.copyProperties(user, sysUserDTO);

        return new LoginUserDTO(user.getId(),
                user.getDeptId(),
                sysUserDTO,
                permissionService.getMenuPermission(sysUserDTO));
    }
}