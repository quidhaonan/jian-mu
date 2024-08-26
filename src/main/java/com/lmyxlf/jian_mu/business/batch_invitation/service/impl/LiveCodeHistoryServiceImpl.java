package com.lmyxlf.jian_mu.business.batch_invitation.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.batch_invitation.dao.LiveCodeHistoryDao;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeHistory;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 14:28
 * @description
 * @since 17
 */
@Slf4j
@Service("liveCodeHistoryService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LiveCodeHistoryServiceImpl extends ServiceImpl<LiveCodeHistoryDao, LiveCodeHistory> implements LiveCodeHistoryService {

    @Override
    public Map<Integer, Long> getViewedTodayCount(List<Integer> liveCodeIds) {
        log.info("获得今日已查看次数，liveCodeIds：{}", liveCodeIds);

        if (CollUtil.isEmpty(liveCodeIds)) {
            log.info("活码 id 集合为空，返回空数据，liveCodeIds：{}", liveCodeIds);
            return new HashMap<>();
        }
        List<LiveCodeHistory> liveCodeHistoryList = this.lambdaQuery()
                .select(LiveCodeHistory::getLiveCodeId)
                .in(LiveCodeHistory::getLiveCodeId, liveCodeIds)
                .ge(LiveCodeHistory::getViewedTime, DateUtil.beginOfDay(new Date()))
                .list();

        return liveCodeHistoryList.stream().collect(Collectors.groupingBy(
                // 根据 liveCodeId 进行分组
                LiveCodeHistory::getLiveCodeId,
                // 计算每个分组的个数
                Collectors.counting()
        ));
    }
}