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
 * @date 2024/9/30 18:33
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysPost extends PageEntity {

    /**
     * 主键 id
     */
    @Excel(name = "岗位序号", cellType = ColumnType.NUMERIC)
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 岗位编码
     */
    @Excel(name = "岗位编码")
    @NotBlank(message = "岗位编码不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符", groups = {Insert.class, Update.class})
    private String postCode;

    /**
     * 岗位名称
     */
    @Excel(name = "岗位名称")
    @NotBlank(message = "岗位名称不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符", groups = {Insert.class, Update.class})
    private String postName;

    /**
     * 显示顺序
     */
    @Excel(name = "岗位排序")
    @NotNull(message = "显示顺序不能为空", groups = {Insert.class, Update.class})
    private Integer postSort;

    /**
     * 状态，0：正常，1：停用
     */
    @Excel(name = "状态", readConverterExp = "0：正常，1：停用")
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}