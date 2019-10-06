package pers.adlered.liteftpd.variable;

import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.enums.Types;
import pers.adlered.liteftpd.user.status.Online;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Variable bean.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class OnlineUserController {
    public static void printOnline() {
        int ipSize = Online.ipRuleOnline.size();
        int userSize = Online.userRuleOnline.size();
        if (ipSize != userSize) {
            ipSize = -1;
        }
        Logger.log(Types.SYS, Levels.INFO, "Online: " + ipSize);
    }

    public static void reduceOnline(String ipAddress, String username) { ;
        for (int i = 0; i < Online.ipRuleOnline.size(); i++) {
            if (Online.ipRuleOnline.get(i).getIp().equals(ipAddress)) {
                Online.ipRuleOnline.remove(i);
                break;
            }
        }
        for (int i = 0; i < Online.userRuleOnline.size(); i++) {
            if (Online.userRuleOnline.get(i).getUsername().equals(username)) {
                Online.userRuleOnline.remove(i);
                break;
            }
        }
    }
}
