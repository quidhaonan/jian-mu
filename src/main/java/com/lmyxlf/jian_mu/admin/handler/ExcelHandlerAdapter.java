package com.lmyxlf.jian_mu.admin.handler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 22:06
 * @description excel 数据格式处理适配器
 * @since 17
 */
public interface ExcelHandlerAdapter {

    /**
     * 格式化
     *
     * @param value 单元格数据值
     * @param args  excel 注解 args 参数组
     * @param cell  单元格对象
     * @param wb    工作簿对象
     * @return 处理后的值
     */
    Object format(Object value, String[] args, Cell cell, Workbook wb);
}