package com.lmyxlf.jian_mu.business.own_tools.common;

import com.lmyxlf.jian_mu.business.own_tools.constant.OTConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/29 17:36
 * @description Async 在同方法调用不生效
 * @since 17
 */
@Slf4j
@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class AsyncMethods {

    private final RedissonClient redissonClient;

    @Async("async_executor_ot")
    public Boolean copyAndRenameFile(Path tempDir, Path path, String randomStr, List<String> suffixs) {
        suffixs.forEach(item -> {
            String originalName = path.getFileName().toString();
            int dotIndex = originalName.lastIndexOf('.');
            String baseName = originalName.substring(0, dotIndex);
            String extension = originalName.substring(dotIndex);

            String newName = baseName + "_" + item + extension;
            Path targetPath = Paths.get(tempDir.toString(), newName);
            try {
                Files.copy(path, targetPath);
            } catch (IOException e) {
                log.error("复制失败，originalName：{}，suffixs：{}", originalName, item);
            }
        });
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(OTConstant.REDIS_COUNTDOWNLATCH_PREFIX + randomStr);
        countDownLatch.countDown();
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error("删除源文件失败，tempDir：{}，path：{}，randomStr：{}，suffixs：{}", tempDir, path, randomStr, suffixs);
        }
        return Boolean.TRUE;
    }
}