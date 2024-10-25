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
 * @date 2024/9/15 3:13
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysConfig extends PageEntity {

    /**
     * 主键 id，配置 id
     */
    @Excel(name = "参数主键", cellType = ColumnType.NUMERIC)
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 参数名称
     */
    @Excel(name = "参数名称")
    @NotBlank(message = "参数名称不能为空", groups = {Insert.class, Update.class})
    @Size(max = 50, message = "参数名称不能超过 50 个字符", groups = {Insert.class, Update.class})
    private String configName;

    /**
     * 参数键名
     */
    @Excel(name = "参数键名")
    @NotBlank(message = "参数键名长度不能为空", groups = {Insert.class, Update.class})
    @Size(max = 50, message = "参数键名长度不能超过 50 个字符", groups = {Insert.class, Update.class})
    private String configKey;

    /**
     * 参数键值
     */
    @Excel(name = "参数键值")
    @NotBlank(message = "参数键值不能为空", groups = {Insert.class, Update.class})
    @Size(max = 255, message = "参数键值长度不能超过 255 个字符", groups = {Insert.class, Update.class})
    private String configValue;

    /**
     * 是否是系统内置，0：否，1：是
     */
    @Excel(name = "系统内置", readConverterExp = "0：否，1：是")
    private Integer configType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}