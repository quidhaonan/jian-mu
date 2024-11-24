package com.lmyxlf.jian_mu.business.batch_invitation.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/10 1:56
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespLiveCodeLocal {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 活码名称
     */
    private String name;

    /**
     * 访问链接
     */
    private String url;

    /**
     * 已查看次数
     */
    private Integer viewedCount;

    /**
     * 状态，0：正常，1：关闭
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}