package com.lmyxlf.jian_mu.business.batch_invitation.model.req;

import com.lmyxlf.jian_mu.global.model.PageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/10 1:56
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqLiveCodeLocal extends PageEntity {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 主键 id 集合
     */
    private List<Integer> ids;

    /**
     * 活码名称
     */
    private String name;

    /**
     * 状态，0：正常，1：关闭
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}