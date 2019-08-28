package pers.adlered.liteftpd.tool;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Status {
    public static String memoryUsed() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        return (memoryUsage.getUsed() / 1048576) + "MB";
    }
}
