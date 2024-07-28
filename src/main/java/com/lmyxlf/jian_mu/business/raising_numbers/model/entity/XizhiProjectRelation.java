package com.lmyxlf.jian_mu.business.raising_numbers.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 3:03
 * @description xizhi 能通知的项目
 * @since 17
 */
@Data
@Accessors(chain = true)
public class XizhiProjectRelation {

    /**
     * 主键 id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * xizhi_notice 表主键 id
     */
    private Integer xizhiId;

    /**
     * project_type 表主键 id
     */
    private Integer projectId;
}