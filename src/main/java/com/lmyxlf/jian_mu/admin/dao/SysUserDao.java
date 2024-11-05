package com.lmyxlf.jian_mu.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmyxlf.jian_mu.admin.annotation.DataScope;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 5:16
 * @description
 * @since 17
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {

    /**
     * 根据条件分页查询用户列表
     *
     * @param reqSysUser 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "dept", userAlias = "user")
    IPage<RespSysUser> selectUserList(@Param("reqSysUser") ReqSysUser reqSysUser, @Param("page") Page<RespSysUser> page);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param reqSysUser 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "dept", userAlias = "user")
    List<SysUser> selectAllocatedList(@Param("reqSysUser") ReqSysUser reqSysUser, @Param("page") Page<SysUser> page);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param reqSysUser 用户信息
     * @return 用户信息集合信息
     */
    @DataScope(deptAlias = "dept", userAlias = "user")
    List<SysUser> selectUnallocatedList(ReqSysUser reqSysUser, Page<SysUser> page);
}