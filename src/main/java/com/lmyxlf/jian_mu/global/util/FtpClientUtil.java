package com.lmyxlf.jian_mu.global.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

@Slf4j
public class FtpClientUtil {

    /**
     * key：FTP 地址+端口+账号+密码 MD5 加密
     * value：FTP 客户端
     */
    private static final Cache<String, FTPClient> FTP_CLIENTS_MAP =
            Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.DAYS).maximumSize(100).build();
    /**
     * key：FTP 客户端 MD5 加密
     * value：FTP 客户端工作目录，默认："/"
     */
    private static final Cache<String, String> FTP_PATH_MAP =
            Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.DAYS).maximumSize(100).build();

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String DEFAULT_WORKING_PATH = "/";

    public static final String ANONYMOUS_USERNAME = "anonymous";

    /**
     * 连接 FTP Server
     *
     * @throws IOException
     */

    public FTPClient connect(String host, Integer port, String username, String password) throws Exception {
        log.info("FTP 服务器连接，host：{}，port：{}，username：{}，password：{}", host, port, username, password);

        String encryptStr = MD5Utils.encrypt(String.format(host, port, username, password));
        FTPClient client = FTP_CLIENTS_MAP.getIfPresent(encryptStr);
        if (ObjUtil.isNotNull(client) && client.isConnected()) {
            log.info("FTP 服务器已连接，返回已存在连接，encryptStr：{}", encryptStr);
            return client;
        }

        FTPClient ftpClient = new FTPClient();
        // 编码
        ftpClient.setControlEncoding(DEFAULT_ENCODING);

        try {
            // 连接 FTP Server
            ftpClient.connect(host, port);
            // 登陆
            if (StrUtil.isEmpty(username)) {
                // 使用匿名登陆
                log.info("FTP 服务器使用匿名登录，host：{}，port：{}，username：{}，password：{}"
                        , host, port, username, password);
                ftpClient.login(ANONYMOUS_USERNAME, ANONYMOUS_USERNAME);
            } else {
                ftpClient.login(username, password);
            }
            // 设置文件格式
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 获取 FTP Server 应答
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                log.error("FTP 服务器连接失败，host：{}，port：{}，username：{}，password：{}"
                        , host, port, username, password);
                throw new LmyXlfException(CODE_MSG.OTHER_SERVICE_UNAVAILABLE);
            }

            FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);
            // 使用被动模式设为默认
            ftpClient.enterLocalPassiveMode();
            // 二进制文件支持
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置模式
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

            FTP_CLIENTS_MAP.put(encryptStr, ftpClient);
            FTP_PATH_MAP.put(MD5Utils.encrypt(JSONUtil.toJsonStr(ftpClient)), DEFAULT_WORKING_PATH);
        } catch (IOException e) {
            log.error("FTP 服务器连接失败，host：{}，port：{}，username：{}，password：{}，e：{}"
                    , host, port, username, password, e.getMessage());
            throw new LmyXlfException(CODE_MSG.ERROR);
        }
        return ftpClient;
    }

    /**
     * 获取当前 FTP 所在目录位置
     *
     * @return
     */
    public String getHome(FTPClient ftpClient) {
        log.info("FTP 服务器获得所在目录位置，ftpClient：{}", ftpClient);

        return FTP_PATH_MAP.getIfPresent(MD5Utils.encrypt(JSONUtil.toJsonStr(ftpClient)));
    }

    /**
     * 断开 FTP 连接
     *
     * @param ftpClient
     * @throws IOException
     */
    public void close(FTPClient ftpClient) {
        log.info("FTP 服务器断开连接，ftpClient：{}", ftpClient);

        if (ObjUtil.isNotNull(ftpClient) && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("FTP 服务器关闭失败，e：{}", e.getMessage());
            }
        }
    }

    /**
     * 获取文件列表
     *
     * @return
     */
    public List<FTPFile> list(FTPClient ftpClient) {
        List<FTPFile> list = null;
        try {
            String ftpPath = FTP_PATH_MAP.getIfPresent(MD5Utils.encrypt(JSONUtil.toJsonStr(ftpClient)));
            FTPFile ff[] = ftpClient.listFiles(ftpPath);
            if (ff != null && ff.length > 0) {
                list = new ArrayList<FTPFile>(ff.length);
                Collections.addAll(list, ff);
            } else {
                list = new ArrayList<FTPFile>(0);
            }
        } catch (Exception e) {
            log.error("FTP 获取文件失败，ftpClient：{}，e：{}", ftpClient, e.getMessage());
        }

        log.info("FTP 服务器获得文件列表，ftpClient：{}，list：{}", ftpClient, list);
        return list;
    }

    /**
     * 切换目录
     *
     * @param path           需要切换的目录
     * @param forcedIncrease 如果目录不存在，是否增加
     */
    public void switchDirectory(FTPClient ftpClient, String path, boolean forcedIncrease) {
        log.info("FTP 服务器切换目录，ftpClient：{}，path：{}，forcedIncrease：{}", ftpClient, path, forcedIncrease);

        try {
            if (StrUtil.isNotEmpty(path)) {
                boolean ok = ftpClient.changeWorkingDirectory(path);
                if (ok) {
                    log.info("FTP 服务器切换目录成功，path：{}", path);
                    String encryptPath = MD5Utils.encrypt(JSONUtil.toJsonStr(ftpClient));
                    FTP_PATH_MAP.put(encryptPath, path);
                } else if (forcedIncrease) {
                    // ftpPath 不存在，手动创建目录
                    log.info("FTP 服务器目录不存在，手动创建，path：{}", path);
                    StringTokenizer token = new StringTokenizer(path, "\\//");
                    while (token.hasMoreTokens()) {
                        String npath = token.nextToken();
                        ftpClient.makeDirectory(npath);
                        ok = ftpClient.changeWorkingDirectory(npath);
                        if (ok) {
                            log.info("FTP 服务器切换目录成功，path：{}", path);
                            String encryptPath = MD5Utils.encrypt(JSONUtil.toJsonStr(ftpClient));
                            FTP_PATH_MAP.put(encryptPath, path);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("FTP 切换目录失败，ftpClient：{}，path：{}，forcedIncrease：{}，e：{}"
                    , ftpClient, path, forcedIncrease, e.getMessage());
        }
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public void createDirectory(FTPClient ftpClient, String path) {
        log.info("FTP 服务器创建目录，ftpClient：{}，path：{}", ftpClient, path);

        try {
            if (StrUtil.isNotEmpty(path)) {
                boolean ok = ftpClient.changeWorkingDirectory(path);
                if (!ok) {
                    // ftpPath 不存在，手动创建目录
                    log.info("FTP 创建目录不存在，手动创建目录，path：{}", path);
                    StringTokenizer token = new StringTokenizer(path, "\\//");
                    while (token.hasMoreTokens()) {
                        String npath = token.nextToken();
                        ftpClient.makeDirectory(npath);
                        ftpClient.changeWorkingDirectory(npath);
                    }
                }
            }
            // 切换为最初目录
            String encryptPath = MD5Utils.encrypt(JSONUtil.toJsonStr(ftpClient));
            String ftpPath = FTP_PATH_MAP.getIfPresent(encryptPath);
            ftpClient.changeWorkingDirectory(ftpPath);
        } catch (Exception e) {
            log.error("FTP 创建目录失败，ftpClient：{}，path：{}，e：{}"
                    , ftpClient, path, e.getMessage());
        }
    }

    /**
     * 删除目录，如果目录中存在文件或者文件夹则删除失败
     *
     * @param path
     * @return
     */
    public boolean deleteDirectory(FTPClient ftpClient, String path) {
        log.info("FTP 服务器删除目录，ftpClient：{}，path：{}", ftpClient, path);

        try {
            return ftpClient.removeDirectory(path);
        } catch (Exception e) {
            log.error("FTP 删除目录失败，ftpClient：{}，path：{}：{}，e：{}"
                    , ftpClient, path, e.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public boolean deleteFile(FTPClient ftpClient, String path) {
        log.info("FTP 服务器删除文件，ftpClient：{}，path：{}", ftpClient, path);

        try {
            return ftpClient.deleteFile(path);
        } catch (Exception e) {
            log.error("FTP 删除文件失败，ftpClient：{}，path：{}：{}，e：{}"
                    , ftpClient, path, e.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 上传文件，上传文件只会传到当前所在目录
     *
     * @param localFile 本地文件
     * @return
     */
    public boolean upload(FTPClient ftpClient, File localFile) {
        return this.upload(ftpClient, localFile, "");
    }

    /**
     * 上传文件，会覆盖 FTP 上原有文件
     *
     * @param localFile 本地文件
     * @param reName    重名
     * @return
     */
    public boolean upload(FTPClient ftpClient, File localFile, String reName) {
        log.info("FTP 服务器上传文件，ftpClient：{}，localFile：{}", ftpClient, localFile);

        String targetName = reName;
        // 设置上传后文件名
        if (StrUtil.isEmpty(reName)) {
            targetName = localFile.getName();
            log.info("FTP 服务器上传文件，使用文件本身名称，targetName：{}", targetName);
        }
        FileInputStream fis = null;
        try {
            // 开始上传文件
            fis = new FileInputStream(localFile);
            ftpClient.setControlEncoding(DEFAULT_ENCODING);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            return ftpClient.storeFile(targetName, fis);
        } catch (Exception e) {
            log.error("FTP 上传文件失败，ftpClient：{}，localFile：{}，reName：{}，e：{}"
                    , ftpClient, localFile, reName, e.getMessage());
        }
        return Boolean.FALSE;
    }

    /**
     * 下载文件，如果存在会覆盖原文件
     *
     * @param ftpFileName 文件名称，FTP 上的文件名称
     * @param savePath    保存目录，本地保存目录
     * @return
     */
    public boolean download(FTPClient ftpClient, String ftpFileName, String savePath) {
        log.info("FTP 服务器下载文件，ftpClient：{}，ftpFileName：{}，savePath：{}", ftpClient, ftpFileName, savePath);

        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileOutputStream fos = null;
        try {
            String saveFile = dir.getAbsolutePath() + File.separator + ftpFileName;
            fos = new FileOutputStream(new File(saveFile));
            return ftpClient.retrieveFile(ftpFileName, fos);
        } catch (Exception e) {
            log.error("FTP 下载文件失败，ftpClient：{}，ftpFileName：{}，savePath：{}，e：{}"
                    , ftpClient, ftpFileName, savePath, e.getMessage());
        }
        return Boolean.FALSE;
    }
}