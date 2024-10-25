package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 2:43
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class ReqSysDept {

    /**
     * 主键 id
     */
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     *
     */
    private Integer parentId;

    /**
     * 父部门 id
     */
    private String ancestors;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符", groups = {Insert.class, Update.class})
    private String deptName;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空", groups = {Insert.class, Update.class})
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符", groups = {Insert.class, Update.class})
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符", groups = {Insert.class, Update.class})
    private String email;

    /**
     * 部门状态，0：正常，1：停用
     */
    private Integer status;
}