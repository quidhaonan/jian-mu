package com.lmyxlf.jian_mu.log;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class LogListenerConfig {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogListenerConfig.class);

    private static final String LOGGER_TAG = "logging.level.";
    private static final String JAVA_LOG_LEVEL_NAMESPACE = "DEV.Java.Log.Level";
    private static final LogLevel DEFAULT_LEVEL = LogLevel.INFO;
    private static final String ROOT_LOG = "root";
    private static final String LOG_NAME_DELIMITER = ".";

    private final LoggingSystem loggingSystem;
    private Config config;

    public LogListenerConfig(LoggingSystem loggingSystem) {
        this.loggingSystem = loggingSystem;
    }

    @PostConstruct
    private void init() {
        this.config = ConfigService.getConfig(JAVA_LOG_LEVEL_NAMESPACE);
        refreshLoggingLevels(config.getPropertyNames());
    }

    @ApolloConfigChangeListener(JAVA_LOG_LEVEL_NAMESPACE)
    private void onChange(ConfigChangeEvent changeEvent) {
        Set<String> changeKeys = changeEvent.changedKeys();
        log.info("java日志等级配置改变：{}", changeKeys);
        refreshLoggingLevels(changeKeys);
    }

    /**
     * 刷新指定的log等级
     * @param changeKeys 修改的log
     */
    private void refreshLoggingLevels(Set<String> changeKeys) {
        for (String key : changeKeys) {
            if (key.startsWith(LOGGER_TAG)) {
                LogLevel level = getLogLevel(key);
                String logName = key.replace(LOGGER_TAG, "");
                loggingSystem.setLogLevel(logName, level);
                log.info("修改日志等级：{}:{}", key, level);
            }
        }
    }

    /**
     * 从apollo配置中心获取log的等级，如果apollo配置中心不存在此配置，则获取父包的等级
     * @param logName log名称
     * @return 日志等级
     */
    private LogLevel getLogLevel(String logName) {
        Set<String> logNameSet = config.getPropertyNames();
        while (!logNameSet.contains(logName) && !ROOT_LOG.equals(logName)) {
            int indexOf = logName.lastIndexOf(LOG_NAME_DELIMITER);
            if (indexOf > LOGGER_TAG.length()) {
                logName = logName.substring(0, indexOf);
            } else {
                logName = ROOT_LOG;
            }
        }
        return config.getEnumProperty(logName, LogLevel.class, DEFAULT_LEVEL);
    }

}