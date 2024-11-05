package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysPostDao;
import com.lmyxlf.jian_mu.admin.model.entity.*;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysPost;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysPost;
import com.lmyxlf.jian_mu.admin.service.SysPostService;
import com.lmyxlf.jian_mu.admin.service.SysUserPostService;
import com.lmyxlf.jian_mu.admin.util.ExcelUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 18:48
 * @description
 * @since 17
 */
@Slf4j
@Service("sysPostService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysPostServiceImpl extends ServiceImpl<SysPostDao, SysPost> implements SysPostService {

    private final SysUserPostService sysUserPostService;

    @Override
    public Page<RespSysPost> list(ReqSysPost reqSysPost) {

        Integer page = reqSysPost.getPage();
        Integer size = reqSysPost.getSize();
        String postCode = reqSysPost.getPostCode();
        String postName = reqSysPost.getPostName();
        Integer status = reqSysPost.getStatus();

        LambdaQueryChainWrapper<SysPost> sysPostLambdaQueryChainWrapper = this.lambdaQuery()
                .like(StrUtil.isNotEmpty(postCode), SysPost::getPostCode, postCode)
                .like(StrUtil.isNotEmpty(postName), SysPost::getPostName, postName)
                .eq(ObjUtil.isNotNull(status), SysPost::getStatus, status)
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME);

        Page<SysPost> sysPostPage = this.page(
                new Page<>(page, size), sysPostLambdaQueryChainWrapper.getWrapper());


        // 仅为将返回对象转为 Resp
        List<RespSysPost> records = sysPostPage.getRecords().stream()
                .map(item -> {
                    RespSysPost respSysPost = new RespSysPost();
                    BeanUtils.copyProperties(item, respSysPost);
                    return respSysPost;
                })
                .collect(Collectors.toList());
        Page<RespSysPost> result = new Page<>(
                sysPostPage.getCurrent(), sysPostPage.getSize(), sysPostPage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public void export(ReqSysPost reqSysPost, HttpServletResponse response) {

        String postCode = reqSysPost.getPostCode();
        String postName = reqSysPost.getPostName();
        Integer status = reqSysPost.getStatus();

        List<SysPost> list = this.lambdaQuery()
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(postCode), SysPost::getPostCode, postCode)
                .like(StrUtil.isNotEmpty(postName), SysPost::getPostName, postName)
                .eq(ObjUtil.isNotNull(status), SysPost::getStatus, status)
                .list();

        ExcelUtil<SysPost> util = new ExcelUtil<>(SysPost.class);
        util.exportExcel(response, list, "岗位数据");
    }

    @Override
    public RespSysPost getInfo(Integer id) {

        SysPost sysPost = this.lambdaQuery()
                .eq(SysPost::getId, id)
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();

        RespSysPost respSysPost = new RespSysPost();
        BeanUtils.copyProperties(sysPost, respSysPost);

        return respSysPost;
    }

    @Override
    public Boolean add(ReqSysPost reqSysPost) {

        SysPost sysPost = new SysPost();
        BeanUtils.copyProperties(reqSysPost, sysPost);

        return this.save(sysPost);
    }

    @Override
    public Boolean edit(ReqSysPost reqSysPost) {

        SysPost sysPost = new SysPost();
        BeanUtils.copyProperties(reqSysPost, sysPost);

        return this.updateById(sysPost);
    }

    @Override
    @Transactional
    public Boolean remove(ReqSysPost reqSysPost) {

        List<Integer> ids = reqSysPost.getIds();

        List<SysPost> sysPostList = this.lambdaQuery()
                .in(SysPost::getId, ids)
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();

        // 查询岗位 在 sys_user_post 中是否被使用
        List<SysUserPost> sysUserPostList = sysUserPostService.lambdaQuery()
                .in(SysUserPost::getPostId, ids)
                .eq(SysUserPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        // key：id，value：出现次数
        Map<Integer, Long> postIdCountsMap = sysUserPostList.stream()
                .collect(Collectors.groupingBy(SysUserPost::getId, Collectors.counting()));

        sysPostList.forEach(item -> {

            if (postIdCountsMap.getOrDefault(item.getId(), 0L) > 0) {

                log.warn("岗位已分配,不能删除，reqSysPost：{}", reqSysPost);
                throw new LmyXlfException("岗位已分配,不能删除");
            }
            item.setDeleteTime(LocalDateTimeUtil.now());
        });

        return this.updateBatchById(sysPostList);
    }

    @Override
    public List<RespSysPost> optionselect() {

        List<SysPost> sysPostList = this.lambdaQuery()
                .eq(SysPost::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();

        // 仅为将返回对象转为 Resp
        return sysPostList.stream().map(item -> {
            RespSysPost respSysPost = new RespSysPost();
            BeanUtils.copyProperties(item, respSysPost);
            return respSysPost;
        }).toList();
    }
}