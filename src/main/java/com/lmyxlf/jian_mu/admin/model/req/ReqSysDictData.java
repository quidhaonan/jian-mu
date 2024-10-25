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
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/13 0:25
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysDictData extends PageEntity {

    /**
     * 主键 id，字典编码
     */
    @Excel(name = "字典编码", cellType = ColumnType.NUMERIC)
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 字典排序
     */
    @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Excel(name = "字典标签")
    @NotBlank(message = "字典标签不能为空", groups = {Insert.class, Update.class})
    @Size(max = 255, message = "字典标签长度不能超过255个字符", groups = {Insert.class, Update.class})
    private String dictLabel;

    /**
     * 字典键值
     */
    @Excel(name = "字典键值")
    @NotBlank(message = "字典键值不能为空", groups = {Insert.class, Update.class})
    @Size(max = 255, message = "字典键值长度不能超过255个字符", groups = {Insert.class, Update.class})
    private String dictValue;

    /**
     * 字典类型
     */
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空", groups = {Insert.class, Update.class})
    @Size(max = 255, message = "字典类型长度不能超过255个字符", groups = {Insert.class, Update.class})
    private String dictType;

    /**
     * 样式属性，其他样式扩展
     */
    @Size(min = 0, max = 255, message = "样式属性长度不能超过255个字符", groups = {Insert.class, Update.class})
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 是否默认，0：否，1：是
     */
    @Excel(name = "是否默认", readConverterExp = "0：否，1：是")
    private Integer isDefault;

    /**
     * 状态，0：正常，1：停用
     */
    @Excel(name = "状态", readConverterExp = "0：正常，1：停用")
    private Integer status;

    private String remark;


    public boolean getDefault() {

        return Integer.valueOf(0).equals(this.isDefault);
    }
}