package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.dao.SysDictDataDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysDictData;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDictData;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictData;
import com.lmyxlf.jian_mu.admin.service.SysDictDataService;
import com.lmyxlf.jian_mu.admin.util.DictUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/20 22:49
 * @description
 * @since 17
 */
@Slf4j
@Service("sysDictDataService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictData> implements SysDictDataService {

    @Override
    public Page<RespSysDictData> list(ReqSysDictData reqSysDictData) {

        Integer page = reqSysDictData.getPage();
        Integer size = reqSysDictData.getSize();
        String dictType = reqSysDictData.getDictType();
        String dictLabel = reqSysDictData.getDictLabel();
        Integer status = reqSysDictData.getStatus();

        LambdaQueryChainWrapper<SysDictData> sysDictDataLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(StrUtil.isNotEmpty(dictType), SysDictData::getDictType, dictType)
                .like(StrUtil.isNotEmpty(dictLabel), SysDictData::getDictLabel, dictLabel)
                .eq(ObjUtil.isNotNull(status), SysDictData::getStatus, status)
                .orderByAsc(SysDictData::getDictSort);

        Page<SysDictData> sysDictDataPage = this.page(
                new Page<>(page, size), sysDictDataLambdaQueryChainWrapper.getWrapper());


        // 仅为将返回对象转为 Resp
        List<RespSysDictData> records = sysDictDataPage.getRecords().stream()
                .map(item -> {
                    RespSysDictData respSysDictData = new RespSysDictData();
                    BeanUtils.copyProperties(item, respSysDictData);
                    return respSysDictData;
                })
                .collect(Collectors.toList());
        Page<RespSysDictData> result = new Page<>(
                sysDictDataPage.getCurrent(), sysDictDataPage.getSize(), sysDictDataPage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public void export(ReqSysDictData reqSysDictData, HttpServletResponse response) {

        String dictType = reqSysDictData.getDictType();
        String dictLabel = reqSysDictData.getDictLabel();
        Integer status = reqSysDictData.getStatus();

        List<SysDictData> list = this.lambdaQuery()
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(StrUtil.isNotEmpty(dictType), SysDictData::getDictType, dictType)
                .like(StrUtil.isNotEmpty(dictLabel), SysDictData::getDictLabel, dictLabel)
                .eq(ObjUtil.isNotNull(status), SysDictData::getStatus, status)
                .orderByAsc(SysDictData::getDictSort)
                .list();

        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    @Override
    public RespSysDictData getInfo(Integer id) {

        SysDictData sysDictData = this.lambdaQuery()
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(SysDictData::getId, id)
                .one();
        if (ObjUtil.isNull(sysDictData)) {

            log.warn("不存在的字典，id：{}", id);
            throw new LmyXlfException("不存在的字典");
        }

        RespSysDictData respSysDictData = new RespSysDictData();
        BeanUtils.copyProperties(sysDictData, respSysDictData);

        return respSysDictData;
    }

    @Override
    public List<RespSysDictData> dictType(String dictType) {

        List<SysDictData> dictCache = DictUtil.getDictCache(dictType);

        if (CollUtil.isNotEmpty(dictCache)) {

            return dictCache.stream().map(item -> {
                RespSysDictData respSysDictData = new RespSysDictData();
                BeanUtils.copyProperties(item, respSysDictData);
                return respSysDictData;
            }).collect(Collectors.toList());
        }
        List<SysDictData> list = this.lambdaQuery()
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(SysDictData::getStatus, UserConstant.NORMAL)
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort)
                .list();

        if (CollUtil.isEmpty(list)) {

            log.warn("根据字典类型查询字典数据信息查询失败，dictType：{}", dictType);
            return new ArrayList<>();
        }

        return list.stream().map(item -> {
            RespSysDictData respSysDictData = new RespSysDictData();
            BeanUtils.copyProperties(item, respSysDictData);
            return respSysDictData;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean add(ReqSysDictData reqSysDictData) {

        SysDictData sysDictData = new SysDictData();
        BeanUtils.copyProperties(reqSysDictData, sysDictData);

        boolean save = this.save(sysDictData);
        if (save) {

            List<SysDictData> list = this.lambdaQuery()
                    .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                    .eq(SysDictData::getStatus, UserConstant.NORMAL)
                    .eq(SysDictData::getDictType, reqSysDictData.getDictType())
                    .orderByAsc(SysDictData::getDictSort)
                    .list();
            DictUtil.setDictCache(reqSysDictData.getDictType(), list);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public Boolean edit(ReqSysDictData reqSysDictData) {

        Integer id = reqSysDictData.getId();

        SysDictData dbSysDictData = this.lambdaQuery()
                .eq(SysDictData::getId, id)
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(dbSysDictData)) {

            log.warn("不存在此字典数据，reqSysDictData：{}", reqSysDictData);
            throw new LmyXlfException("不存在此字典数据");
        }
        SysDictData sysDictData = new SysDictData();
        BeanUtils.copyProperties(reqSysDictData, sysDictData);

        boolean flag = this.updateById(sysDictData);
        if (flag) {

            List<SysDictData> list = this.lambdaQuery()
                    .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                    .eq(SysDictData::getStatus, UserConstant.NORMAL)
                    .eq(SysDictData::getDictType, reqSysDictData.getDictType())
                    .orderByAsc(SysDictData::getDictSort)
                    .list();
            DictUtil.setDictCache(reqSysDictData.getDictType(), list);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public Boolean remove(ReqSysDictData reqSysDictData) {

        List<Integer> ids = reqSysDictData.getIds();

        List<SysDictData> list = this.lambdaQuery()
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .in(SysDictData::getId, ids)
                .list();
        list.forEach(item -> {
            List<SysDictData> sysDictDataList = this.lambdaQuery()
                    .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                    .eq(SysDictData::getStatus, UserConstant.NORMAL)
                    .eq(SysDictData::getDictType, reqSysDictData.getDictType())
                    .orderByAsc(SysDictData::getDictSort)
                    .list();
            DictUtil.setDictCache(reqSysDictData.getDictType(), sysDictDataList);
            item.setDeleteTime(LocalDateTimeUtil.now());
        });

        return this.updateBatchById(list);
    }
}