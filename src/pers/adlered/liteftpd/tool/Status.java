package pers.adlered.liteftpd.tool;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Get information of the server.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Status {
    public static String memoryUsed() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        return (memoryUsage.getUsed() / 1048576) + "MB";
    }
}
