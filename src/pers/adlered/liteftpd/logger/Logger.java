package pers.adlered.liteftpd.logger;

import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.logger.enums.Types;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>All logs will through here.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 15:18
 **/
public class Logger {
    public static boolean log(Types type, Levels level, String log) {
        if (Filter.fil(level)) {
            // Can be logged
            System.out.println("[" + level + "]" + " " + "[" + type + "]" + " >> " + log);
            return true;
        } else {
            // Cannot log
            return false;
        }
    }
}
