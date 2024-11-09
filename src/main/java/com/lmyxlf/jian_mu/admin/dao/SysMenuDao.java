package com.lmyxlf.jian_mu.admin.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmyxlf.jian_mu.admin.model.entity.SysMenu;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysMenu;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 23:21
 * @description
 * @since 17
 */
@Mapper
@DS(DBConstant.ADMIN)
public interface SysMenuDao extends BaseMapper<SysMenu> {

    /**
     * 根据用户 id 查询菜单
     *
     * @param userId 用户 id
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param reqSysMenu 菜单信息
     * @return 菜单列表
     */
    List<SysMenu> selectMenuListByUserId(ReqSysMenu reqSysMenu);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色 id
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    List<Integer> selectMenuListByRoleId(@Param("roleId") Integer roleId, @Param("menuCheckStrictly") Integer menuCheckStrictly);
}