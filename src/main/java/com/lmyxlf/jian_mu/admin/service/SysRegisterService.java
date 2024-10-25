package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.req.ReqLoginBody;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 13:18
 * @description
 * @since 17
 */
public interface SysRegisterService {

    Boolean register(ReqLoginBody reqLoginBody);
}