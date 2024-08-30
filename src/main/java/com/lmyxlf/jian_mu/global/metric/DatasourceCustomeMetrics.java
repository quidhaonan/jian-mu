package com.lmyxlf.jian_mu.global.metric;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Component
public class DatasourceCustomeMetrics implements MeterBinder {

    @Autowired
    private DataSource dataSource;

    @Override
    public void bindTo(@NotNull MeterRegistry meterRegistry) {

        if (dataSource instanceof DynamicRoutingDataSource dynamicRoutingDataSource) {
            Map<String, DataSource> dataSourceMap = dynamicRoutingDataSource.getDataSources();

            Gauge.builder("datasource.active.count", () -> getActiveCount(dataSourceMap))
                    .baseUnit("活跃数量")
                    .description("当前项目数据源活跃数量")
                    .register(meterRegistry);
            Gauge.builder("datasource.pooling.count", () -> getPoolingCount(dataSourceMap))
                    .baseUnit("池化计数")
                    .description("当前项目数据源池化计数")
                    .register(meterRegistry);
            Gauge.builder("datasource.create.count", () -> getCreateCount(dataSourceMap))
                    .baseUnit("创建计数")
                    .description("当前项目数据源创建计数")
                    .register(meterRegistry);
            Gauge.builder("datasource.destroy.count", () -> getDestroyCount(dataSourceMap))
                    .baseUnit("销毁计数")
                    .description("当前项目数据源销毁计数")
                    .register(meterRegistry);
            Gauge.builder("datasource.connect.count", () -> getConnectCount(dataSourceMap))
                    .baseUnit("连接计数")
                    .description("当前项目数据源连接计数")
                    .register(meterRegistry);
        }


    }

    @SneakyThrows
    private Long getActiveCount(Map<String, DataSource> dataSourceMap) {
        // 活跃数量
        long activeCount = 0;
        for (String key : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(key);
            DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
            activeCount += druidDataSource.getActiveCount();
        }
        return activeCount;
    }

    @SneakyThrows
    private Long getPoolingCount(Map<String, DataSource> dataSourceMap) {
        // 池化计数
        long poolingCount = 0;
        for (String key : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(key);
            DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
            poolingCount += druidDataSource.getPoolingCount();
        }
        return poolingCount;
    }

    @SneakyThrows
    private Long getCreateCount(Map<String, DataSource> dataSourceMap) {
        // 创建计数
        long createCount = 0;
        for (String key : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(key);
            DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
            createCount += druidDataSource.getCreateCount();
        }
        return createCount;
    }

    @SneakyThrows
    private Long getDestroyCount(Map<String, DataSource> dataSourceMap) {
        // 销毁计数
        long destroyCount = 0;
        for (String key : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(key);
            DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
            destroyCount += druidDataSource.getDestroyCount();
        }
        return destroyCount;
    }

    @SneakyThrows
    private Long getConnectCount(Map<String, DataSource> dataSourceMap) {
        // 连接计数
        long connectCount = 0;
        for (String key : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(key);
            DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
            connectCount += druidDataSource.getConnectCount();
        }
        return connectCount;
    }
}