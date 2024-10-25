package com.lmyxlf.jian_mu.admin.util;

import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysRoleDTO;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 14:21
 * @description 安全服务工具类
 * @since 17
 */
@Slf4j
public class SecurityUtil {

    /**
     * 用户 id
     **/
    public static Integer getUserId() {

        try {

            return getLoginUser().getId();
        } catch (Exception e) {

            log.error("获取用户 id 异常，e：{}", e.getMessage());
            throw new LmyXlfException(CODE_MSG.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门 id
     **/
    public static Integer getDeptId() {

        try {

            return getLoginUser().getDeptId();
        } catch (Exception e) {

            log.error("获取部门 id 异常，e：{}", e.getMessage());
            throw new LmyXlfException(CODE_MSG.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {

        try {

            return getLoginUser().getUsername();
        } catch (Exception e) {

            log.error("获取用户账户异常，e：{}", e.getMessage());
            throw new LmyXlfException(CODE_MSG.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUserDTO getLoginUser() {

        try {

            return (LoginUserDTO) getAuthentication().getPrincipal();
        } catch (Exception e) {

            log.error("获取用户信息异常，e：{}", e.getMessage());
            throw new LmyXlfException(CODE_MSG.UNAUTHORIZED);
        }
    }

    /**
     * 获取 Authentication
     */
    public static Authentication getAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成 BCryptPasswordEncoder 密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户 id
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {

        return userId != null && 1L == userId;
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(String permission) {

        return hasPermi(getLoginUser().getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermi(Collection<String> authorities, String permission) {

        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> AdminConstant.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(String role) {

        List<SysRoleDTO> roleList = getLoginUser().getSysUserDTO().getSysRoleDTOS();
        Collection<String> roles = roleList.stream().map(SysRoleDTO::getRoleKey).collect(Collectors.toSet());
        return hasRole(roles, role);
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public static boolean hasRole(Collection<String> roles, String role) {

        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> AdminConstant.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}