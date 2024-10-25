package com.lmyxlf.jian_mu.admin.model.entity;

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
 * @date 2024/10/24 0:34
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("代码生成业务字段表 gen_table_column")
public class GenTableColumn {

    @TableId
    @ApiModelProperty(value = "主键 id")
    private Integer id;

    @ApiModelProperty(value = "表名称")
    private Integer tableId;

    @ApiModelProperty(value = "表名称")
    private String columnName;

    @ApiModelProperty(value = "表名称")
    private String columnComment;

    @ApiModelProperty(value = "表名称")
    private String columnType;

    @ApiModelProperty(value = "表名称")
    private String javaType;

    @ApiModelProperty(value = "表名称")
    private String javaField;

    @ApiModelProperty(value = "表名称")
    private Integer isPk;

    @ApiModelProperty(value = "表名称")
    private Integer isIncrement;

    @ApiModelProperty(value = "表名称")
    private Integer isRequired;

    @ApiModelProperty(value = "表名称")
    private Integer isInsert;

    @ApiModelProperty(value = "表名称")
    private Integer isEdit;

    @ApiModelProperty(value = "表名称")
    private Integer isList;

    @ApiModelProperty(value = "表名称")
    private Integer isQuery;

    @ApiModelProperty(value = "表名称")
    private String queryType;

    @ApiModelProperty(value = "表名称")
    private String htmlType;

    @ApiModelProperty(value = "表名称")
    private String dictType;

    @ApiModelProperty(value = "表名称")
    private Integer sort;

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
}