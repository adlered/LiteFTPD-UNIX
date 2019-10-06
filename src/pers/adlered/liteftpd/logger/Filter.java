package pers.adlered.liteftpd.logger;

import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.variable.Variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>When Logger received a log, Filter will check it's level to decide display or not.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 15:17
 **/
public class Filter {
    public static boolean fil(Levels level) {
        boolean status = false;
        switch (level) {
            case DEBUG:
                if (Variable.debugLevel >= 4) {
                    status = true;
                }
                break;
            case ERROR:
                if (Variable.debugLevel >= 3) {
                    status = true;
                }
                break;
            case WARN:
                if (Variable.debugLevel >= 2) {
                    status = true;
                }
                break;
            case INFO:
                if (Variable.debugLevel >= 1) {
                    status = true;
                }
                break;
        }
        return status;
    }
}
