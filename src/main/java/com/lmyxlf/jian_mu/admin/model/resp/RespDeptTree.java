package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/18 0:38
 * @description 角色部门树列表
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespDeptTree {

    private List<Integer> checkedKeys;

    private List<RespTreeSelect> depts;
}