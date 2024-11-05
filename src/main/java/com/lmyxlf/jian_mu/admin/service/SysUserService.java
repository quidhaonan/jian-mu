package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysUser;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysUser;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 5:16
 * @description
 * @since 17
 */
public interface SysUserService extends IService<SysUser> {

    IPage<RespSysUser> list(ReqSysUser reqSysUser);

    void export(ReqSysUser reqSysUser, HttpServletResponse response);

    String importData(MultipartFile file, boolean updateSupport) throws Exception;

    void importTemplate(HttpServletResponse response);

    RespSysUser getInfo(Integer id);

    Boolean add(ReqSysUser reqSysUser);

    Boolean edit(ReqSysUser reqSysUser);

    Boolean remove(ReqSysUser reqSysUser);

    Boolean resetPwd(ReqSysUser reqSysUser);

    Boolean changeStatus(ReqSysUser reqSysUser);

    RespSysUser authRole(Integer id);

    Boolean insertAuthRole(ReqSysUser reqSysUser);

    /**
     * 根据用户 id 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    String selectUserRoleGroup(String userName);

    /**
     * 根据用户 id 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    String selectUserPostGroup(String userName);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param reqSysUser 用户信息
     * @return 用户信息集合信息
     */
    Page<SysUser> selectAllocatedList(ReqSysUser reqSysUser);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param reqSysUser 用户信息
     * @return 用户信息集合信息
     */
    Page<SysUser> selectUnallocatedList(ReqSysUser reqSysUser);
}