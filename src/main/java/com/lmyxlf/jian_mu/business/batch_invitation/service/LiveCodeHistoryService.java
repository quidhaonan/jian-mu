package com.lmyxlf.jian_mu.business.batch_invitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeHistory;

import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 14:27
 * @description
 * @since 17
 */
public interface LiveCodeHistoryService extends IService<LiveCodeHistory> {

    /**
     * 获得今日已查看次数
     *
     * @param liveCodeIds
     * @return
     */
    Map<Integer, Long> getViewedTodayCount(List<Integer> liveCodeIds);
}