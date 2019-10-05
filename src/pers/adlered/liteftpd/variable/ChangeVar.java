package pers.adlered.liteftpd.variable;

import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;
import pers.adlered.liteftpd.user.status.IpLimitBind;
import pers.adlered.liteftpd.user.status.Online;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Variable bean.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class ChangeVar {
    public static void printOnline() {
        Logger.log(Types.SYS, Levels.INFO, "Online: " + Online.ipRuleOnline.size());
    }

    public static void reduceOnlineIP(String IPAddress) {
        String target = "127.0.0.1";
        int index = 0;
        for (IpLimitBind ip : Online.ipRuleOnline) {
            if (ip.getIp().equals(target)) {
                Online.ipRuleOnline.remove(index);
                break;
            }
            ++index;
        }
    }
}
