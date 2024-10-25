package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.dto.SysUserOnlineDTO;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/15 1:06
 * @description
 * @since 17
 */
public interface SysUserOnlineService {

    List<SysUserOnlineDTO> list(String ipaddr, String userName);

    Boolean forceLogout(String tokenId);
}