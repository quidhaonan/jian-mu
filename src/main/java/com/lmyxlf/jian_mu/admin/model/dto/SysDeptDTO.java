package com.lmyxlf.jian_mu.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 21:42
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("security 保存用户信息")
public class SysDeptDTO {

    @ApiModelProperty(value = "主键 id，部门 id")
    private Integer id;

    @ApiModelProperty(value = "父部门 id")
    private Integer parentId;

    @ApiModelProperty(value = "祖级列表")
    private String ancestors;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门状态，0：正常，1：停用")
    private Integer status;

    @ApiModelProperty(value = "创建者")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新者")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteTime;

    @ApiModelProperty(value = "父部门名称")
    private String parentName;

    @ApiModelProperty(value = "子部门")
    private List<SysDeptDTO> children = new ArrayList<>();
}