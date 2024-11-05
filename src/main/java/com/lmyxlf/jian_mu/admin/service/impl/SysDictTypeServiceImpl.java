package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.dao.SysDictDataDao;
import com.lmyxlf.jian_mu.admin.dao.SysDictTypeDao;
import com.lmyxlf.jian_mu.admin.model.entity.SysConfig;
import com.lmyxlf.jian_mu.admin.model.entity.SysDictData;
import com.lmyxlf.jian_mu.admin.model.entity.SysDictType;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDictType;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictData;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysDictType;
import com.lmyxlf.jian_mu.admin.service.SysDictDataService;
import com.lmyxlf.jian_mu.admin.service.SysDictTypeService;
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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/20 23:15
 * @description
 * @since 17
 */
@Slf4j
@Service("sysDictTypeService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeDao, SysDictType> implements SysDictTypeService {

    private final SysDictDataService sysDictDataService;

    @Override
    public Page<RespSysDictType> list(ReqSysDictType reqSysDictType) {

        Integer page = reqSysDictType.getPage();
        Integer size = reqSysDictType.getSize();
        String dictName = reqSysDictType.getDictName();
        String dictType = reqSysDictType.getDictType();
        Integer status = reqSysDictType.getStatus();
        LocalDateTime beginTime = reqSysDictType.getBeginTime();
        LocalDateTime endTime = reqSysDictType.getEndTime();

        LambdaQueryChainWrapper<SysDictType> sysDictTypeLambdaQueryChainWrapper = this.lambdaQuery()
                .eq(SysDictType::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(dictName), SysDictType::getDictName, dictName)
                .like(StrUtil.isNotEmpty(dictType), SysDictType::getDictType, dictType)
                .eq(ObjUtil.isNotNull(status), SysDictType::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), SysDictType::getCreateTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysDictType::getCreateTime, endTime);

        Page<SysDictType> sysDictTypePage = this.page(
                new Page<>(page, size), sysDictTypeLambdaQueryChainWrapper.getWrapper());


        // 仅为将返回对象转为 Resp
        List<RespSysDictType> records = sysDictTypePage.getRecords().stream()
                .map(item -> {
                    RespSysDictType respSysDictType = new RespSysDictType();
                    BeanUtils.copyProperties(item, respSysDictType);
                    return respSysDictType;
                })
                .collect(Collectors.toList());
        Page<RespSysDictType> result = new Page<>(
                sysDictTypePage.getCurrent(), sysDictTypePage.getSize(), sysDictTypePage.getTotal());
        result.setRecords(records);

        return result;
    }

    @Override
    public void export(ReqSysDictType reqSysDictType, HttpServletResponse response) {

        String dictName = reqSysDictType.getDictName();
        String dictType = reqSysDictType.getDictType();
        Integer status = reqSysDictType.getStatus();
        LocalDateTime beginTime = reqSysDictType.getBeginTime();
        LocalDateTime endTime = reqSysDictType.getEndTime();

        List<SysDictType> list = this.lambdaQuery()
                .eq(SysDictType::getDeleteTime, DBConstant.INITIAL_TIME)
                .like(StrUtil.isNotEmpty(dictName), SysDictType::getDictName, dictName)
                .like(StrUtil.isNotEmpty(dictType), SysDictType::getDictType, dictType)
                .eq(ObjUtil.isNotNull(status), SysDictType::getStatus, status)
                .ge(ObjUtil.isNotNull(beginTime), SysDictType::getCreateTime, beginTime)
                .le(ObjUtil.isNotNull(endTime), SysDictType::getCreateTime, endTime)
                .list();

        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    @Override
    public RespSysDictType getInfo(Integer id) {

        SysDictType sysDictType = this.lambdaQuery()
                .eq(SysDictType::getDeleteTime, DBConstant.INITIAL_TIME)
                .eq(SysDictType::getId, id)
                .one();
        if (ObjUtil.isNull(sysDictType)) {

            log.warn("不存在的字典类型，id：{}", id);
            throw new LmyXlfException("不存在的字典类型");
        }

        RespSysDictType respSysDictType = new RespSysDictType();
        BeanUtils.copyProperties(sysDictType, respSysDictType);

        return respSysDictType;
    }

    @Override
    public Boolean add(ReqSysDictType reqSysDictType) {

        SysDictType sysDictType = new SysDictType();
        BeanUtils.copyProperties(reqSysDictType, sysDictType);

        boolean save = this.save(sysDictType);
        if (save) {

            DictUtil.setDictCache(reqSysDictType.getDictType(), null);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public Boolean edit(ReqSysDictType reqSysDictType) {

        Integer id = reqSysDictType.getId();
        String dictType = reqSysDictType.getDictType();

        SysDictType dbSysDictType = this.lambdaQuery()
                .eq(SysDictType::getId, id)
                .eq(SysDictType::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(dbSysDictType)) {

            log.warn("不存在此字典数据类型，reqSysDictType：{}", reqSysDictType);
            throw new LmyXlfException("不存在此字典数据类型");
        }

        // 修改 sys_dict_data
        boolean dataFlag = sysDictDataService.lambdaUpdate()
                .set(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictType, dbSysDictType.getDictType())
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        SysDictType sysDictType = new SysDictType();
        BeanUtils.copyProperties(reqSysDictType, sysDictType);

        boolean typeFlag = this.updateById(sysDictType);

        if (dataFlag && typeFlag) {

            List<SysDictData> list = sysDictDataService.lambdaQuery()
                    .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                    .eq(SysDictData::getStatus, UserConstant.NORMAL)
                    .eq(SysDictData::getDictType, dictType)
                    .orderByAsc(SysDictData::getDictSort)
                    .list();
            DictUtil.setDictCache(dictType, list);

            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public Boolean remove(ReqSysDictType reqSysDictType) {

        List<Integer> ids = reqSysDictType.getIds();

        List<SysDictType> sysDictTypeList = this.lambdaQuery()
                .eq(SysDictType::getDeleteTime, DBConstant.INITIAL_TIME)
                .in(SysDictType::getId, ids)
                .list();

        // 查询 dictType 在 sys_dict_data 中是否被使用
        List<String> dictTypes = sysDictTypeList.stream()
                .map(SysDictType::getDictType)
                .toList();
        List<SysDictData> sysDictDataList = sysDictDataService.lambdaQuery()
                .in(SysDictData::getDictType, dictTypes)
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        // key：dictType，value：出现次数
        Map<String, Long> dictTypeCountsMap = sysDictDataList.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType, Collectors.counting()));

        sysDictTypeList.forEach(item -> {
            if (dictTypeCountsMap.getOrDefault(item.getDictType(), 0L) > 0) {

                log.warn("数据字段类型已被分配，不能删除，reqSysDictType：{}", reqSysDictType);
                throw new LmyXlfException("数据字段类型已被分配，不能删除");
            }
            DictUtil.removeDictCache(item.getDictType());
            item.setDeleteTime(LocalDateTimeUtil.now());
        });

        return this.updateBatchById(sysDictTypeList);
    }

    @Override
    public Boolean refreshCache() {

        DictUtil.clearDictCache();
        List<SysDictData> sysDictDataList = sysDictDataService.lambdaQuery()
                .eq(SysDictData::getStatus, UserConstant.NORMAL)
                .eq(SysDictData::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        Map<String, List<SysDictData>> dictDataMap = sysDictDataList.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType));

        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {

            DictUtil.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }

        return Boolean.TRUE;
    }

    @Override
    public List<RespSysDictType> optionselect() {

        List<SysDictType> list = this.lambdaQuery()
                .eq(SysDictType::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();

        // 仅为将返回对象转为 Resp
        return list.stream().map(item -> {
            RespSysDictType respSysDictType = new RespSysDictType();
            BeanUtils.copyProperties(item, respSysDictType);
            return respSysDictType;
        }).toList();
    }
}