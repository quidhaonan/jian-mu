package com.lmyxlf.jian_mu.business.batch_invitation.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/10 0:38
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("活码表 live_code_local")
public class LiveCodeLocal {

    @TableId
    @ApiModelProperty(value = "主键 id")
    private Integer id;

    @ApiModelProperty(value = "活码名称")
    private String name;

    @ApiModelProperty(value = "用户 id")
    private Integer userId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "原始文件名称")
    private String fileOriginalName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "随机字符串")
    private String randomStr;

    @ApiModelProperty(value = "已查看次数")
    private Integer viewedCount;

    @ApiModelProperty(value = "状态，0：正常，1：关闭")
    private Integer status;

    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}