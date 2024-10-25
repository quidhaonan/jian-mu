package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.admin.annotation.Excel;
import com.lmyxlf.jian_mu.admin.annotation.Excel.ColumnType;
import com.lmyxlf.jian_mu.global.model.PageEntity;
import com.lmyxlf.jian_mu.global.validation.group.Delete;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/21 14:36
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysDictType extends PageEntity {

    /**
     * 主键 id
     */
    @Excel(name = "字典类型主键", cellType = ColumnType.NUMERIC)
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 字典名称
     */
    @Excel(name = "字典名称")
    @NotBlank(message = "字典名称不能为空", groups = {Insert.class, Update.class})
    @Size(max = 100, message = "字典类型名称长度不能超过100个字符", groups = {Insert.class, Update.class})
    private String dictName;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空", groups = {Insert.class, Update.class})
    @Size(max = 100, message = "字典类型类型长度不能超过100个字符", groups = {Insert.class, Update.class})
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）", groups = {Insert.class, Update.class})
    private String dictType;

    /**
     * 状态，0：正常，1：停用
     */
    @Excel(name = "状态", readConverterExp = "状态，0：正常，1：停用")
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