package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.admin.annotation.Excel;
import com.lmyxlf.jian_mu.admin.annotation.Excel.ColumnType;
import com.lmyxlf.jian_mu.global.model.PageEntity;
import com.lmyxlf.jian_mu.global.validation.group.Delete;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Other;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 18:00
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysRole extends PageEntity {

    /**
     * 主键 id
     */
    @Excel(name = "角色序号", cellType = ColumnType.NUMERIC)
    @Null(message = "主键 id 应为空", groups = {Insert.class, Other.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 用户 id
     */
    private Integer userId;

    /**
     * 用户 id 集合
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Other.class})
    private List<Integer> userIds;

    /**
     * 角色名称
     */
    @Excel(name = "角色名称")
    @NotBlank(message = "角色名称不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符", groups = {Insert.class, Update.class})
    private String roleName;

    /**
     * 角色权限字符串
     */
    @Excel(name = "角色权限")
    @NotBlank(message = "权限字符不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符", groups = {Insert.class, Update.class})
    private String roleKey;

    /**
     * 显示顺序
     */
    @Excel(name = "角色排序")
    @NotNull(message = "显示顺序不能为空", groups = {Insert.class, Update.class})
    private Integer roleSort;

    /**
     * 数据范围，1：全部数据权限，2：自定数据权限，3：本部门数据权限，4：本部门及以下数据权限
     */
    @Excel(name = "数据范围", readConverterExp = "1：全部数据权限，2：自定数据权限，3：本部门数据权限，4：本部门及以下数据权限,5：仅本人数据权限")
    private Integer dataScope;

    /**
     * 角色状态，0：正常，1：停用
     */
    @Excel(name = "角色状态", readConverterExp = "0：正常，1：停用")
    private Integer status;

    /**
     * 菜单组
     */
    private List<Integer> menuIds;

    /**
     * 部门组（数据权限）
     */
    private List<Integer> deptIds;

    /**
     * 角色菜单权限
     */
    private Set<String> permissions;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    public boolean isAdmin() {

        return isAdmin(this.id);
    }

    public static boolean isAdmin(Integer roleId) {

        return Integer.valueOf(1).equals(roleId);
    }
}