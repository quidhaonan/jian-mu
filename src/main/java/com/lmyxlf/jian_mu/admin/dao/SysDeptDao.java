package com.lmyxlf.jian_mu.admin.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmyxlf.jian_mu.admin.annotation.DataScope;
import com.lmyxlf.jian_mu.admin.model.entity.SysDept;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDept;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDept;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 2:06
 * @description
 * @since 17
 */
@Mapper
@DS(DBConstant.ADMIN)
public interface SysDeptDao extends BaseMapper<SysDept> {

    /**
     * 查询部门管理数据
     *
     * @param reqSysDept 部门信息
     * @return 部门信息集合
     */
    @DataScope(deptAlias = "dept")
    List<RespSysDept> selectDeptList(ReqSysDept reqSysDept);

    /**
     * 根据部门 id 查询信息
     *
     * @param id 部门 id
     * @return 部门信息
     */
    SysDept selectDeptById(@Param("id") Integer id);

    /**
     * 根据 id 查询所有子部门（正常状态）
     *
     * @param id 部门 id
     * @return 子部门数
     */
    Integer selectNormalChildrenDeptById(@Param("id") Integer id);

    /**
     * 根据 id 查询所有子部门
     *
     * @param id 部门 id
     * @return 部门列表
     */
    List<SysDept> selectChildrenDeptById(@Param("id") Integer id);

    /**
     * 根据角色 id 查询部门树信息
     *
     * @param roleId            角色 id
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    List<Integer> selectDeptListByRoleId(@Param("roleId") Integer roleId, @Param("deptCheckStrictly") Integer deptCheckStrictly);
}