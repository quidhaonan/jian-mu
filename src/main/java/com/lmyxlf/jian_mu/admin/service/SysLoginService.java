package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.req.ReqLoginBody;
import com.lmyxlf.jian_mu.admin.model.resp.RespLoginUserInfo;
import com.lmyxlf.jian_mu.admin.model.resp.RespRouter;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/22 1:01
 * @description 登录校验类
 * @since 17
 */
public interface SysLoginService {

    /**
     * 登录验证
     *
     * @return token
     */
    String login(ReqLoginBody reqLoginBody);

    RespLoginUserInfo getInfo();

    List<RespRouter> getRouters();
}