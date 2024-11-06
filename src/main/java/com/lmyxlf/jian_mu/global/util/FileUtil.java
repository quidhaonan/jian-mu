package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.constant.MimeTypeConstant;
import com.lmyxlf.jian_mu.global.constant.TraceConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/3 22:38
 * @description 文件处理工具类
 * @since 17
 */
@Slf4j
public class FileUtil {

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPG = "image/jpg";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_BMP = "image/bmp";
    public static final String IMAGE_GIF = "image/gif";
    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};
    public static final String[] FLASH_EXTENSION = {"swf", "flv"};
    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};
    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};
    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;
    /**
     * 默认大小 4G
     */
    public static final long DEFAULT_MAX_SIZE = 4 * 1024 * 1024 * 1024L;

    /**
     * 输出指定文件的 byte 数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {

        FileInputStream fis = null;
        try {

            File file = new File(filePath);
            if (!file.exists()) {

                log.warn("输出指定文件的 byte 数组，文件路径不存在，filePath：{}", file);
                throw new LmyXlfException(CODE_MSG.ERROR);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {

                os.write(b, 0, length);
            }
        } catch (IOException e) {

            log.error("输出指定文件的 byte 数组，出现异常，filePath：{}，e：{}", filePath, e.getMessage());
            throw new LmyXlfException(CODE_MSG.ERROR);
        } finally {

            IOUtils.close(os);
            IOUtils.close(fis);
        }
    }

    /**
     * 写数据到文件中
     *
     * @param data      数据
     * @param uploadDir 目标文件
     * @return 目标文件
     * @throws IOException IO异常
     */
    public static String writeBytes(byte[] data, String uploadDir) throws IOException {

        FileOutputStream fos = null;
        String pathName;
        try {

            String extension = getFileExtendName(data);
            pathName = DateUtil.format(DateUtil.date(), "yyyy/MM/dd") + "/" + UUID.fastUUID() + "." + extension;
            File file = getAbsoluteFile(uploadDir, pathName);
            fos = new FileOutputStream(file);
            fos.write(data);
        } finally {

            IOUtils.close(fos);
        }

        return getPathFileName(uploadDir, pathName);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {

        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {

            flag = file.delete();
        }

        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {

        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource) {

        // 禁止目录上跳级别
        if (StrUtil.contains(resource, "..")) {

            return Boolean.FALSE;
        }

        // 检查是否允许下载的文件规则
        return ArrayUtils.contains(MimeTypeConstant.DEFAULT_ALLOWED_EXTENSION, getFileType(resource));
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {

        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {

            // IE 浏览器
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {

            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {

            // google 浏览器
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        } else {

            // 其它浏览器
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        }

        return filename;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {

        String percentEncodedFileName = percentEncode(realFileName);

        String contentDispositionValue = "attachment; filename=" +
                percentEncodedFileName +
                ";" +
                "filename*=" +
                "utf-8''" +
                percentEncodedFileName;
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {

        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8);
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 获取图像后缀
     *
     * @param photoByte 图像数据
     * @return 后缀名
     */
    public static String getFileExtendName(byte[] photoByte) {

        String strFileExtendName = "jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97)) {

            strFileExtendName = "gif";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70)) {

            strFileExtendName = "jpg";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {

            strFileExtendName = "bmp";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {

            strFileExtendName = "png";
        }

        return strFileExtendName;
    }

    /**
     * 获取文件名称
     * /test/jian_mu.png --> jian_mu.png
     *
     * @param fileName 路径名称
     * @return 没有文件路径的名称
     */
    public static String getName(String fileName) {

        if (fileName == null) {

            return null;
        }
        int lastUnixPos = fileName.lastIndexOf('/');
        int lastWindowsPos = fileName.lastIndexOf('\\');
        int index = Math.max(lastUnixPos, lastWindowsPos);

        return fileName.substring(index + 1);
    }

    /**
     * 获取不带后缀文件名称
     * /test/jian_mu.png --> jian_mu
     *
     * @param fileName 路径名称
     * @return 没有文件路径和后缀的名称
     */
    public static String getNameNotSuffix(String fileName) {

        if (fileName == null) {

            return null;
        }

        return FilenameUtils.getBaseName(fileName);
    }


    /**
     * 获取文件类型
     * jian_mu.png --> png
     *
     * @param file 文件名
     * @return 后缀（不含".")
     */
    public static String getFileType(File file) {

        if (null == file) {

            return StringUtils.EMPTY;
        }

        return getFileType(file.getName());
    }

    /**
     * 获取文件类型
     * jian_mu.png --> png
     *
     * @param fileName 文件名
     * @return 后缀（不含".")
     */
    public static String getFileType(String fileName) {

        int separatorIndex = fileName.lastIndexOf(".");
        if (separatorIndex < 0) {

            return "";
        }

        return fileName.substring(separatorIndex + 1).toLowerCase();
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {

        try {

            return upload(baseDir, file, DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {

            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws IOException {

        int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNamelength > DEFAULT_FILE_NAME_LENGTH) {

            throw new LmyXlfException("文件名过长");
        }

        assertAllowed(file, allowedExtension);
        String fileName = extractFilename(file);
        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
        file.transferTo(Paths.get(absPath));

        return getPathFileName(baseDir, fileName);
    }

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {

        String traceId = MDC.get(TraceConstant.TRACE_ID);
        if (StrUtil.isBlank(traceId)) {

            traceId = UUID.fastUUID().toString(true);
        }

        return StrUtil.format("{}/{}_{}.{}", DateUtil.format(DateUtil.date(),
                FilenameUtils.getBaseName(file.getOriginalFilename())), traceId, getExtension(file));
    }

    public static File getAbsoluteFile(String uploadDir, String fileName) {

        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists()) {

            if (!desc.getParentFile().exists()) {

                desc.getParentFile().mkdirs();
            }
        }

        return desc;
    }

    public static String getPathFileName(String uploadDir, String fileName) {

        int dirLastIndex = System.getProperty("user.dir").length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return currentDir + "/" + fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     */
    public static void assertAllowed(MultipartFile file, String[] allowedExtension) {

        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {

            throw new LmyXlfException("文件过大");
        }

        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {

            if (allowedExtension == IMAGE_EXTENSION) {

                throw new LmyXlfException("不被允许的文件类型：" + Arrays.toString(IMAGE_EXTENSION));
            } else if (allowedExtension == FLASH_EXTENSION) {

                throw new LmyXlfException("不被允许的文件类型：" + Arrays.toString(FLASH_EXTENSION));
            } else if (allowedExtension == MEDIA_EXTENSION) {

                throw new LmyXlfException("不被允许的文件类型：" + Arrays.toString(MEDIA_EXTENSION));
            } else if (allowedExtension == VIDEO_EXTENSION) {

                throw new LmyXlfException("不被允许的文件类型：" + Arrays.toString(VIDEO_EXTENSION));
            } else {

                throw new LmyXlfException("不被允许的文件类型");
            }
        }
    }

    /**
     * 判断 mime 类型是否是允许的 mime 类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {

        for (String str : allowedExtension) {

            if (str.equalsIgnoreCase(extension)) {

                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {

            extension = getExtension(Objects.requireNonNull(file.getContentType()));
        }

        return extension;
    }

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {

            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf"};

    public static String getExtension(String prefix) {

        return switch (prefix) {
            case IMAGE_PNG -> "png";
            case IMAGE_JPG -> "jpg";
            case IMAGE_JPEG -> "jpeg";
            case IMAGE_BMP -> "bmp";
            case IMAGE_GIF -> "gif";
            default -> "";
        };
    }
}