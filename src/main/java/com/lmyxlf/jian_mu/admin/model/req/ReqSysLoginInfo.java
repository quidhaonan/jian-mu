package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.global.model.PageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/8 1:52
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysLoginInfo extends PageEntity {

    /**
     * id 集合
     */
    private List<Integer> ids;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录 ip 地址
     */
    private String loginIp;

    /**
     * 登录状态，0：成功，1：失败
     */
    private Integer status;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}