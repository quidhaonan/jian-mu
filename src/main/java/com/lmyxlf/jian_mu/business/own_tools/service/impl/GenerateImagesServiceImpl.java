package com.lmyxlf.jian_mu.business.own_tools.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.business.own_tools.common.AsyncMethods;
import com.lmyxlf.jian_mu.business.own_tools.constant.OTConstant;
import com.lmyxlf.jian_mu.business.own_tools.model.req.ReqGenerateImages;
import com.lmyxlf.jian_mu.business.own_tools.service.GenerateImagesService;
import com.lmyxlf.jian_mu.global.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 21:36
 * @description
 * @since 17
 */
@Slf4j
@Service("generateImagesService")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GenerateImagesServiceImpl implements GenerateImagesService {

    private final AsyncMethods asyncMethods;
    private final RedissonClient redissonClient;

    @Override
    public void generateImages(ReqGenerateImages reqGenerateImages, MultipartFile file, HttpServletResponse response) throws IOException {
        log.info("批量复制图片，reqGenerateImages：{},fileName：{}，fileSize：{}", reqGenerateImages, file.getOriginalFilename(), file.getSize());
        List<String> suffixs = reqGenerateImages.getSuffixs();

        // 临时目录存储文件
        String primaryPath = System.getProperty("user.dir");
        String randomStr = RandomUtil.generateRandomStr(6);
        Path tempDir = Paths.get(StrUtil.format("{}{}{}{}{}", primaryPath, OTConstant.OWN_TOOLS_PATH, "/temp/", randomStr, "/"));
        Files.createDirectories(tempDir);
        // 临时输出 zip 文件地址
        Path outputZipPath = Paths.get(StrUtil.format("{}{}{}", primaryPath, OTConstant.OWN_TOOLS_PATH, "/result/"));
        Files.createDirectories(outputZipPath);

        // 解压 ZIP 文件
        unzip(file, tempDir.toString());

        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(OTConstant.REDIS_COUNTDOWNLATCH_PREFIX + randomStr);
        // 复制文件
        copyFile(tempDir, suffixs, randomStr);

        try {
            boolean completed = countDownLatch.await(10 * 60, TimeUnit.SECONDS);
            if (!completed) {
                log.error("countDownLatch 等待超时，respBrowserEncrypt：");
            }
        } catch (InterruptedException e) {
            log.error("countDownLatch 出现错误，respBrowserEncrypt：");
        }

        // 压缩文件到新的 ZIP
        String zipFilePathStr = outputZipPath + "/" + randomStr + ".zip";
        toZip(tempDir.toString(), zipFilePathStr);

        // 设置响应内容类型
        response.setContentType("application/zip");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + randomStr + ".zip\"");

        // 使用 try-with-resources 确保所有资源都被正确关闭
        try (FileInputStream fileInputStream = new FileInputStream(Paths.get(zipFilePathStr).toString());
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            // 读取文件并写入响应输出流
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush(); // 确保所有内容都被写出
        } catch (IOException e) {
            log.error("读取文件出错：e：{}", e.getMessage());
        } finally {
            // 清理临时文件
            deleteDirectory(tempDir.toFile());
            deleteDirectory(outputZipPath.toFile());
        }
    }

    /**
     * 解压 ZIP 文件
     *
     * @param file
     * @param destDir
     * @throws IOException
     */
    private static void unzip(MultipartFile file, String destDir) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(file.getInputStream());
        // fixme:不能正常接收名称为中文的图片
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = Paths.get(destDir, entry.getName()).toString();
            if (!entry.isDirectory()) {
                extractFile(zipIn, filePath);
            } else {
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public void copyFile(Path tempDir, List<String> suffixs, String randomStr) throws IOException {
        log.info("线程池：{}", Thread.currentThread().getName());

        try (Stream<Path> files = Files.list(tempDir)) {
            // 统计文件数量
            long fileCount = files.count();
            log.info("即将复制的文件数量：{}", fileCount);
            RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(OTConstant.REDIS_COUNTDOWNLATCH_PREFIX + randomStr);
            countDownLatch.trySetCount(fileCount);

            // 重新获取Stream进行文件操作，因为流已经被消费
            Files.list(tempDir).forEach(path -> {
                asyncMethods.copyAndRenameFile(tempDir, path, randomStr, suffixs);
            });
        }
    }

    /**
     * 压缩文件到新的 ZIP
     *
     * @param folderPath
     * @param zipPath
     * @throws IOException
     */
    private static void toZip(String folderPath, String zipPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipPath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(folderPath);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    /**
     * 删除文件
     *
     * @param directoryToBeDeleted
     */
    private static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}