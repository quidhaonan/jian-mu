package com.lmyxlf.jian_mu.admin.model.resp;

import cn.hutool.core.util.NumberUtil;
import com.lmyxlf.jian_mu.admin.model.dto.*;
import com.lmyxlf.jian_mu.global.util.IPUtils;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 0:54
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("服务器相关信息")
public class RespServer {

    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU 相关信息
     */
    private CpuDTO cpuDTO = new CpuDTO();

    /**
     * 內存相关信息
     */
    private MemDTO memDTO = new MemDTO();

    /**
     * JVM 相关信息
     */
    private JvmDTO jvmDTO = new JvmDTO();

    /**
     * 服务器相关信息
     */
    private SysDTO sysDTO = new SysDTO();

    /**
     * 磁盘相关信息
     */
    private List<SysFileDTO> sysFileDTOS = new LinkedList<>();

    public void copyTo() throws Exception {

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        setCpuInfo(hal.getProcessor());

        setMemInfo(hal.getMemory());

        setSysInfo();

        setJvmInfo();

        setSysFileDTOS(si.getOperatingSystem());
    }

    /**
     * 设置 CPU 信息
     */
    private void setCpuInfo(CentralProcessor processor) {

        // CPU 信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpuDTO.setCpuNum(processor.getLogicalProcessorCount());
        cpuDTO.setTotal(totalCpu);
        cpuDTO.setSys(cSys);
        cpuDTO.setUsed(user);
        cpuDTO.setWait(iowait);
        cpuDTO.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {

        memDTO.setTotal(memory.getTotal());
        memDTO.setUsed(memory.getTotal() - memory.getAvailable());
        memDTO.setFree(memory.getAvailable());
    }

    /**
     * 设置服务器信息
     */
    private void setSysInfo() {

        Properties props = System.getProperties();
        sysDTO.setComputerName(IPUtils.getLocalHostName());
        sysDTO.setComputerIp(IPUtils.getLocalIp());
        sysDTO.setOsName(props.getProperty("os.name"));
        sysDTO.setOsArch(props.getProperty("os.arch"));
        sysDTO.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置 Java 虚拟机
     */
    private void setJvmInfo() {

        Properties props = System.getProperties();
        jvmDTO.setTotal(Runtime.getRuntime().totalMemory());
        jvmDTO.setMax(Runtime.getRuntime().maxMemory());
        jvmDTO.setFree(Runtime.getRuntime().freeMemory());
        jvmDTO.setVersion(props.getProperty("java.version"));
        jvmDTO.setHome(props.getProperty("java.home"));
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFileDTOS(OperatingSystem os) {

        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {

            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFileDTO sysFileDTO = new SysFileDTO();
            sysFileDTO.setDirName(fs.getMount());
            sysFileDTO.setSysTypeName(fs.getType());
            sysFileDTO.setTypeName(fs.getName());
            sysFileDTO.setTotal(convertFileSize(total));
            sysFileDTO.setFree(convertFileSize(free));
            sysFileDTO.setUsed(convertFileSize(used));
            sysFileDTO.setUsage(NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
            sysFileDTOS.add(sysFileDTO);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {

        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {

            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {

            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {

            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {

            return String.format("%d B", size);
        }
    }
}