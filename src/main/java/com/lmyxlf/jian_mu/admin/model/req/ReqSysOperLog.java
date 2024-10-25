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
 * @date 2024/9/14 0:30
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysOperLog extends PageEntity {

    /**
     * id 集合
     */
    private List<Integer> ids;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型，0：其它，1：新增，2：修改，3：删除
     */
    private List<Integer> businessType;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作状态，0：正常，1：异常
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