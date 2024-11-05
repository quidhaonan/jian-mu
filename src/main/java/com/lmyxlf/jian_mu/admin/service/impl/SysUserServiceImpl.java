package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysUserDao;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.*;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysRole;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysRole;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysUser;
import com.lmyxlf.jian_mu.admin.service.*;
import com.lmyxlf.jian_mu.admin.util.BeanValidatorsUtil;
import com.lmyxlf.jian_mu.admin.util.ExcelUtil;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 5:18
 * @description
 * @since 17
 */
@Slf4j
@Service("sysUserService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    private final SysRoleService sysRoleService;
    private final SysUserRoleService sysUserRoleService;
    private final SysPostService sysPostService;
    private final SysUserPostService sysUserPostService;
    private final SysUserDao sysUserDao;
    private final Validator validator;
    private final SysDeptService sysDeptService;
    private final SysConfigService sysConfigService;

    @Override
    public IPage<RespSysUser> list(ReqSysUser reqSysUser) {

        Integer current = reqSysUser.getPage();
        Integer size = reqSysUser.getSize();

        Page<RespSysUser> page = new Page<>(current, size);

        return sysUserDao.selectUserList(reqSysUser, page);
    }

    @Override
    public void export(ReqSysUser reqSysUser, HttpServletResponse response) {

        IPage<RespSysUser> iPage = sysUserDao.selectUserList(reqSysUser, null);
        ExcelUtil<RespSysUser> util = new ExcelUtil<>(RespSysUser.class);
        util.exportExcel(response, iPage.getRecords(), "用户数据");
    }

    @Override
    public String importData(MultipartFile file, boolean updateSupport) throws Exception {

        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());

        return importUser(userList, updateSupport);
    }

    @Override
    public void importTemplate(HttpServletResponse response) {

        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @Override
    public RespSysUser getInfo(Integer id) {

        RespSysUser respSysUser = new RespSysUser();

        checkUserDataScope(id);
        // 查询所有角色
        List<RespSysRole> roles = sysRoleService.optionselect();
        List<SysPost> posts = sysPostService.lambdaQuery()
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();

        respSysUser.setRoles(SysUserDTO.isAdmin(id) ?
                        roles :
                        roles.stream()
                                .filter(item -> !SysUserDTO.isAdmin(item.getId()))
                                .collect(Collectors.toList()))
                .setPosts(posts);
        if (ObjUtil.isNotNull(id)) {

            SysUser sysUser = this.lambdaQuery()
                    .eq(SysUser::getId, id)
                    .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                    .one();
            BeanUtils.copyProperties(sysUser, respSysUser);

            List<SysUserPost> sysUserPostList = sysUserPostService.lambdaQuery()
                    .eq(SysUserPost::getUserId, id)
                    .eq(SysUserPost::getDeleteTime, DBConstant.INITIAL_TIME)
                    .list();
            List<SysUserRole> sysUserRoleList = sysUserRoleService.lambdaQuery()
                    .eq(SysUserRole::getUserId, id)
                    .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                    .list();
            respSysUser
                    .setPostIds(sysUserPostList.stream().map(SysUserPost::getId).toList())
                    .setRoleIds(sysUserRoleList.stream().map(SysUserRole::getId).toList());
        }

        return respSysUser;
    }

    @Override
    public Boolean add(ReqSysUser reqSysUser) {

        Integer id = reqSysUser.getId();
        Integer deptId = reqSysUser.getDeptId();
        List<Integer> roleIds = reqSysUser.getRoleIds();
        String password = reqSysUser.getPassword();
        List<Integer> postIds = reqSysUser.getPostIds();

        sysDeptService.checkDeptDataScope(deptId);
        sysRoleService.checkRoleDataScope(ArrayUtil.toArray(roleIds, Integer.class));

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(reqSysUser, sysUser);
        sysUser.setPassword(SecurityUtil.encryptPassword(password));

        // 新增用户信息
        this.save(sysUser);
        // 新增用户岗位关联
        insertUserPost(id, postIds);
        // 新增用户与角色管理
        insertUserRole(id, roleIds);

        return Boolean.TRUE;
    }

    @Override
    public Boolean edit(ReqSysUser reqSysUser) {

        Integer id = reqSysUser.getId();
        Integer deptId = reqSysUser.getDeptId();
        List<Integer> roleIds = reqSysUser.getRoleIds();
        List<Integer> postIds = reqSysUser.getPostIds();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(reqSysUser, sysUser);

        checkUserAllowed(sysUser);
        checkUserDataScope(id);
        sysDeptService.checkDeptDataScope(deptId);
        sysRoleService.checkRoleDataScope(ArrayUtil.toArray(roleIds, Integer.class));

        // 删除用户与角色关联
        sysUserRoleService.lambdaUpdate()
                .set(SysUserRole::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysUserRole::getUserId, id)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        // 新增用户与角色管理
        insertUserRole(id, roleIds);
        // 删除用户与岗位关联
        sysUserPostService.lambdaUpdate()
                .set(SysUserPost::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysUserPost::getUserId, id)
                .eq(SysUserPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        // 新增用户与岗位管理
        insertUserPost(id, postIds);

        return this.updateById(sysUser);
    }

    @Override
    public Boolean remove(ReqSysUser reqSysUser) {

        List<Integer> ids = reqSysUser.getIds();

        if (CollUtil.contains(ids, SecurityUtil.getUserId())) {

            log.warn("当前用户不能删除：{}", reqSysUser);
            throw new LmyXlfException("当前用户不能删除");
        }

        ids.forEach(item -> {
            checkUserAllowed(new SysUser().setId(item));
            checkUserDataScope(item);
        });
        // 删除用户与角色关联
        sysUserRoleService.lambdaUpdate()
                .set(SysUserRole::getDeleteTime, LocalDateTimeUtil.now())
                .in(SysUserRole::getUserId, ids)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        // 删除用户与岗位关联
        sysUserPostService.lambdaUpdate()
                .set(SysUserPost::getDeleteTime, LocalDateTimeUtil.now())
                .in(SysUserPost::getUserId, ids)
                .eq(SysUserPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        return this.lambdaUpdate()
                .set(SysUser::getDeleteTime, LocalDateTimeUtil.now())
                .in(SysUser::getId, ids)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    public Boolean resetPwd(ReqSysUser reqSysUser) {

        Integer id = reqSysUser.getId();
        String password = reqSysUser.getPassword();

        SysUser sysUser = new SysUser();
        sysUser.setId(id);

        checkUserAllowed(sysUser);
        checkUserDataScope(id);
        sysUser.setPassword(SecurityUtil.encryptPassword(password));

        return this.updateById(sysUser);
    }

    @Override
    public Boolean changeStatus(ReqSysUser reqSysUser) {

        Integer id = reqSysUser.getId();
        Integer status = reqSysUser.getStatus();

        SysUser sysUser = new SysUser();
        sysUser.setId(id).setStatus(status);

        checkUserAllowed(sysUser);
        checkUserDataScope(id);

        return this.updateById(sysUser);
    }

    @Override
    public RespSysUser authRole(Integer id) {

        RespSysUser respSysUser = new RespSysUser();

        SysUser sysUser = this.lambdaQuery()
                .eq(SysUser::getId, id)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        BeanUtils.copyProperties(sysUser, respSysUser);

        List<SysUserRole> sysUserRoleList = sysUserRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, id)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        List<Integer> roleIds = sysUserRoleList.stream().map(SysUserRole::getRoleId).toList();
        List<SysRole> userRoleList = sysRoleService.lambdaQuery()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        List<Integer> userRoleIds = userRoleList.stream().map(SysRole::getId).toList();
        List<RespSysRole> respSysRoleList = sysRoleService.selectRoleList(new ReqSysRole(), null);

        respSysRoleList.forEach(item -> {

            if (userRoleIds.contains(item.getId())) {

                item.setFlag(Boolean.TRUE);
            }
        });

        respSysUser.setRoles(SysUserDTO.isAdmin(id) ?
                respSysRoleList :
                respSysRoleList.stream().filter(r -> !SysUserDTO.isAdmin(r.getId())).collect(Collectors.toList()));

        return respSysUser;
    }

    @Override
    public Boolean insertAuthRole(ReqSysUser reqSysUser) {

        Integer id = reqSysUser.getId();
        List<Integer> roleIds = reqSysUser.getRoleIds();

        checkUserDataScope(id);
        sysRoleService.checkRoleDataScope(ArrayUtil.toArray(roleIds, Integer.class));

        // 删除用户与角色关联
        sysUserRoleService.lambdaUpdate()
                .set(SysUserRole::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysUserRole::getUserId, id)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        // 新增用户与角色管理
        insertUserRole(id, roleIds);

        return Boolean.TRUE;
    }

    @Override
    public String selectUserRoleGroup(String userName) {

        SysUser sysUser = this.lambdaQuery()
                .eq(SysUser::getUserName, userName)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(sysUser)) {

            log.warn("用户不存在，userName：{}", userName);
            throw new LmyXlfException("用户不存在");
        }

        List<SysUserRole> sysUserRoleList = sysUserRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, sysUser.getId())
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        List<Integer> roleIds = sysUserRoleList.stream()
                .map(SysUserRole::getRoleId)
                .toList();

        List<SysRole> sysRoleList = sysRoleService.lambdaQuery()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();

        if (CollUtil.isEmpty(sysRoleList)) {

            return StrUtil.EMPTY;
        }

        return sysRoleList.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    public String selectUserPostGroup(String userName) {

        SysUser sysUser = this.lambdaQuery()
                .eq(SysUser::getUserName, userName)
                .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(sysUser)) {

            log.warn("用户不存在，userName：{}", userName);
            throw new LmyXlfException("用户不存在");
        }

        List<SysUserPost> sysUserPostList = sysUserPostService.lambdaQuery()
                .eq(SysUserPost::getUserId, sysUser.getId())
                .eq(SysUserPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        List<Integer> postIds = sysUserPostList.stream()
                .map(SysUserPost::getPostId)
                .toList();

        List<SysPost> sysPostList = sysPostService.lambdaQuery()
                .in(SysPost::getId, postIds)
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();

        if (CollUtil.isEmpty(sysPostList)) {

            return StrUtil.EMPTY;
        }

        return sysPostList.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    @Override
    public Page<SysUser> selectAllocatedList(ReqSysUser reqSysUser) {

        Integer current = reqSysUser.getPage();
        Integer size = reqSysUser.getSize();

        Page<SysUser> page = new Page<>(current, size);
        sysUserDao.selectAllocatedList(reqSysUser, page);

        return page;
    }

    @Override
    public Page<SysUser> selectUnallocatedList(ReqSysUser reqSysUser) {

        Integer current = reqSysUser.getPage();
        Integer size = reqSysUser.getSize();

        Page<SysUser> page = new Page<>(current, size);
        sysUserDao.selectUnallocatedList(reqSysUser, page);

        return page;
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport) {

        if (CollUtil.isEmpty(userList)) {

            throw new LmyXlfException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysUser user : userList) {

            try {

                // 验证是否存在这个用户
                SysUser u = this.lambdaQuery()
                        .eq(SysUser::getUserName, user.getUserName())
                        .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                        .one();
                if (ObjUtil.isNull(u)) {

                    BeanValidatorsUtil.validateWithException(validator, user);
                    sysDeptService.checkDeptDataScope(user.getDeptId());
                    String password = sysConfigService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtil.encryptPassword(password));
                    this.save(user);
                    successNum++;
                    successMsg.append("<br/>")
                            .append(successNum)
                            .append("、账号 ")
                            .append(user.getUserName())
                            .append(" 导入成功");
                } else if (isUpdateSupport) {

                    BeanValidatorsUtil.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getId());
                    sysDeptService.checkDeptDataScope(user.getDeptId());
                    user.setId(u.getId());
                    this.updateById(user);
                    successNum++;
                    successMsg.append("<br/>")
                            .append(successNum)
                            .append("、账号 ")
                            .append(user.getUserName())
                            .append(" 更新成功");
                } else {

                    failureNum++;
                    failureMsg.append("<br/>")
                            .append(failureNum)
                            .append("、账号 ")
                            .append(user.getUserName())
                            .append(" 已存在");
                }
            } catch (Exception e) {

                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {

            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new LmyXlfException(failureMsg.toString());
        }

        successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    private void checkUserAllowed(SysUser user) {

        if (ObjUtil.isNotNull(user.getId()) && SysUserDTO.isAdmin(user.getId())) {

            throw new LmyXlfException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户 id
     */
    private void checkUserDataScope(Integer userId) {

        if (!SysUserDTO.isAdmin(SecurityUtil.getUserId())) {

            ReqSysUser reqSysUser = new ReqSysUser();
            reqSysUser.setId(userId);
            IPage<RespSysUser> iPage = sysUserDao.selectUserList(reqSysUser, null);
            if (CollUtil.isEmpty(iPage.getRecords())) {

                throw new LmyXlfException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户 id
     * @param roleIds 角色组
     */
    public void insertUserRole(Integer userId, List<Integer> roleIds) {

        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysUserRole> sysUserRoleList = roleIds.stream().map(item -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId).setRoleId(item);
                return sysUserRole;
            }).toList();
            sysUserRoleService.saveBatch(sysUserRoleList);
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param userId  用户 id
     * @param postIds 岗位组
     */
    public void insertUserPost(Integer userId, List<Integer> postIds) {

        if (CollUtil.isNotEmpty(postIds)) {
            List<SysUserPost> sysUserPostList = postIds.stream().map(item -> {
                SysUserPost sysUserPost = new SysUserPost();
                sysUserPost.setUserId(userId).setPostId(item);
                return sysUserPost;
            }).toList();
            sysUserPostService.saveBatch(sysUserPostList);
        }
    }
}