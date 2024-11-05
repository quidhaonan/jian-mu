package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.dao.SysNoticeDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysNotice;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysNotice;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysNotice;
import com.lmyxlf.jian_mu.admin.service.SysNoticeService;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:37
 * @description
 * @since 17
 */
@Slf4j
@Service("sysNoticeService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeDao, SysNotice> implements SysNoticeService {

    @Override
    public Page<RespSysNotice> list(ReqSysNotice reqSysNotice) {

        Integer page = reqSysNotice.getPage();
        Integer size = reqSysNotice.getSize();
        String noticeTitle = reqSysNotice.getNoticeTitle();
        Integer noticeType = reqSysNotice.getNoticeType();
        String createUser = reqSysNotice.getCreateUser();

        LambdaQueryChainWrapper<SysNotice> sysNoticeLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(SysNotice::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(StrUtil.isNotEmpty(noticeTitle), SysNotice::getNoticeTitle, noticeTitle)
                .eq(ObjUtil.isNotNull(noticeType), SysNotice::getNoticeType, noticeType)
                .like(StrUtil.isNotEmpty(createUser), SysNotice::getCreateUser, createUser);

        Page<SysNotice> sysNoticePage = this.page(
                new Page<>(page, size), sysNoticeLambdaQueryChainWrapper.getWrapper());


        // 仅为将返回对象转为 Resp
        List<RespSysNotice> records = sysNoticePage.getRecords().stream()
                .map(item -> {
                    RespSysNotice respSysNotice = new RespSysNotice();
                    BeanUtils.copyProperties(item, respSysNotice);
                    return respSysNotice;
                })
                .collect(Collectors.toList());
        Page<RespSysNotice> result = new Page<>(
                sysNoticePage.getCurrent(), sysNoticePage.getSize(), sysNoticePage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public RespSysNotice getInfo(Integer id) {

        SysNotice sysNotice = this.lambdaQuery()
                .eq(SysNotice::getId, id)
                .eq(SysNotice::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();

        RespSysNotice respSysNotice = new RespSysNotice();
        BeanUtils.copyProperties(sysNotice, respSysNotice);

        return respSysNotice;
    }

    @Override
    public Boolean add(ReqSysNotice reqSysNotice) {

        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyProperties(reqSysNotice, sysNotice);

        return this.save(sysNotice);
    }

    @Override
    public Boolean edit(ReqSysNotice reqSysNotice) {

        SysNotice sysNotice = new SysNotice();
        BeanUtils.copyProperties(reqSysNotice, sysNotice);

        return this.updateById(sysNotice);
    }

    @Override
    public Boolean remove(ReqSysNotice reqSysNotice) {

        List<Integer> ids = reqSysNotice.getIds();

        return this.lambdaUpdate()
                .set(SysNotice::getDeleteTime, LocalDateTimeUtil.now())
                .in(SysNotice::getId, ids)
                .eq(SysNotice::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }
}