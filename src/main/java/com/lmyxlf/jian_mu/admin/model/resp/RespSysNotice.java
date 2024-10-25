package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:32
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespSysNotice {

    /**
     * 主键 id
     */
    private Integer id;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型，0：通知，1：公告
     */
    private Integer noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态，0：正常，1：关闭
     */
    private Integer status;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}