package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.entity.SysUser;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 2:26
 * @description 登录密码
 * @since 17
 */
public interface SysPasswordService {

    void validate(SysUser user);

    boolean matches(SysUser user, String rawPassword);

    void clearLoginRecordCache(String loginName);
}