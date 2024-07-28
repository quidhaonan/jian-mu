package com.lmyxlf.jian_mu.business.raising_numbers.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 2:59
 * @description xizhi 密钥
 * @since 17
 */
@Data
@Accessors(chain = true)
public class XizhiNotice {

    /**
     * 主键 id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 是否删除，0：未删除，1：已删除
     */
    private Integer isDelete;

    /**
     * 备注
     */
    private String remark;
}