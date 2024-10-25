package com.lmyxlf.jian_mu.admin.model.resp;

import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/13 2:04
 * @description 登录用户角色以及权限集合
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespLoginUserInfo {

    /**
     * 登录用户信息
     */
    private SysUserDTO sysUserDTO;

    /**
     * 角色集合
     */
    private Set<String> roles;

    /**
     * permissions
     */
    private Set<String> permissions;
}