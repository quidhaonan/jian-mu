package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysMenu;
import com.lmyxlf.jian_mu.admin.model.entity.SysOperLog;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysMenu;
import com.lmyxlf.jian_mu.admin.model.resp.RespRoleMenuTree;
import com.lmyxlf.jian_mu.admin.model.resp.RespRouter;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysMenu;
import com.lmyxlf.jian_mu.admin.model.resp.RespTreeSelect;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 23:22
 * @description
 * @since 17
 */
public interface SysMenuService extends IService<SysMenu> {

    List<RespSysMenu> list(ReqSysMenu reqSysMenu);

    RespSysMenu getInfo(Integer id);

    List<RespTreeSelect> treeselect(ReqSysMenu reqSysMenu);

    RespRoleMenuTree roleMenuTreeselect(Integer roleId);

    Boolean add(ReqSysMenu reqSysMenu);

    Boolean edit(ReqSysMenu reqSysMenu);

    Boolean remove(Integer id);

    /**
     * 根据用户 id 查询菜单树信息
     *
     * @param userId 用户 id
     * @return 菜单列表
     */
    List<RespSysMenu> selectMenuTreeByUserId(Integer userId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param respSysMenuList 菜单列表
     * @return 路由列表
     */
    List<RespRouter> buildMenus(List<RespSysMenu> respSysMenuList);
}