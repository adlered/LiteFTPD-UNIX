package pers.adlered.liteftpd.variable;

import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Variable bean.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class ChangeVar {
    public static void plusOnlineCount() {
        ++Variable.online;
        Logger.log(Types.SYS, Levels.INFO, "Online: " + Variable.online);
    }

    public static void reduceOnlineCount() {
        --Variable.online;
        Logger.log(Types.SYS, Levels.INFO, "Online: " + Variable.online);
    }
}
