package com.lmyxlf.jian_mu.business.raising_numbers.filter;

import cn.hutool.core.util.ArrayUtil;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.XizhiNotice;
import com.lmyxlf.jian_mu.business.raising_numbers.model.entity.XizhiProjectRelation;
import com.lmyxlf.jian_mu.business.raising_numbers.model.enums.ProjectTypeEnums;
import com.lmyxlf.jian_mu.business.raising_numbers.service.XizhiNoticeService;
import com.lmyxlf.jian_mu.business.raising_numbers.service.XizhiProjectRelationService;
import com.lmyxlf.jian_mu.global.aspect.NoticeErrorAspect;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.constant.SysConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 3:17
 * @description
 * @since 17
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NoticeErrorFilter implements NoticeErrorAspect.PutPushUrlsAble {

    private final XizhiNoticeService xizhiNoticeService;
    private final XizhiProjectRelationService xizhiProjectRelationService;

    private static final List<Integer> projectTypes = new ArrayList<>();

    @Override
    public boolean filter(String[] filter) {
        if (ArrayUtil.isEmpty(filter)) {
            return Boolean.FALSE;
        }
        projectTypes.clear();

        ProjectTypeEnums[] values = ProjectTypeEnums.values();
        if (filter.length == 1 && SysConstant.LMYXLF.equals(filter[0])) {
            projectTypes.addAll(Arrays.stream(values).map(ProjectTypeEnums::getId).toList());
            return Boolean.TRUE;
        }

        List<String> filterList = Arrays.asList(filter);
        for (ProjectTypeEnums item : values) {
            if (filterList.contains(item.getName())) {
                projectTypes.add(item.getId());
            }
        }

        return !projectTypes.isEmpty();
    }

    @Override
    public String[] put() {

        List<XizhiProjectRelation> xizhiProjectRelations = xizhiProjectRelationService.lambdaQuery()
                .in(XizhiProjectRelation::getProjectId, projectTypes)
                .list();
        List<Integer> xizhiIds = xizhiProjectRelations.stream().map(XizhiProjectRelation::getXizhiId).toList();
        List<XizhiNotice> xizhiNotices = xizhiNoticeService.lambdaQuery()
                .in(XizhiNotice::getId, xizhiIds)
                .eq(XizhiNotice::getIsDelete, DBConstant.NOT_DELETED)
                .list();
        log.info("添加推送链接，xizhiNotices：{}", xizhiNotices);

        List<String> secretKeys = xizhiNotices.stream().map(XizhiNotice::getSecretKey).toList();
        return secretKeys.toArray(new String[0]);
    }
}