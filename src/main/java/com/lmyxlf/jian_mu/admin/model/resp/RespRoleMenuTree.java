package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/13 22:09
 * @description 角色菜单树
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespRoleMenuTree {

    private List<Integer> checkedKeys;

    private List<RespTreeSelect> menus;
}