package pers.adlered.liteftpd.user.verify;

import pers.adlered.liteftpd.user.status.IpLimitBind;
import pers.adlered.liteftpd.user.status.Online;
import pers.adlered.liteftpd.variable.Variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>检测用户登录数量限制，返回True或者False决定是否接受连接。</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 14:49
 **/
public class OnlineRules {
    /**
     * 检查IP地址是否可以登录
     *
     * @param ipAddress
     * @return true则允许登录 false则禁止登录
     */
    public static boolean checkIpAddress(String ipAddress) {
        boolean onList = false;
        String onListRule = "";
        int onListLimit = -1;
        String[] ipRules = Variable.ipOnlineLimit.split(";");
        String[] ip = ipAddress.split("\\.");
        for (int i = 0; i < ipRules.length; i += 2) {
            if (ipRules[i].equals("0.0.0.0")) {
                onList = true;
                onListRule = ipRules[i];
                onListLimit = Integer.parseInt(ipRules[i+1]);
                break;
            }
            String[] ruleIp = ipRules[i].split("\\.");
            // 首先，第一位需要相等
            if (ruleIp[0].equals(ip[0]) || ruleIp[0].toLowerCase().equals("x")) {
                if (ruleIp[1].equals(ip[1]) || ruleIp[1].toLowerCase().equals("x")) {
                    if (ruleIp[2].equals(ip[2]) || ruleIp[2].toLowerCase().equals("x")) {
                        if (ruleIp[3].equals(ip[3]) || ruleIp[3].toLowerCase().equals("x")) {
                            onList = true;
                            onListRule = ipRules[i];
                            onListLimit = Integer.parseInt(ipRules[i+1]);
                        }
                    }
                }
            }
        }
        if (onList) {
            if (onListLimit == 0) {
                Online.ipRuleOnline.add(new IpLimitBind(ipAddress, onListRule));
                return true;
            } else {
                int count = 0;
                for (IpLimitBind ipRule : Online.ipRuleOnline) {
                    if (ipRule.getRule().equals(onListRule)) {
                        ++count;
                    }
                }
                if (count < onListLimit) {
                    Online.ipRuleOnline.add(new IpLimitBind(ipAddress, onListRule));
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            Online.ipRuleOnline.add(new IpLimitBind(ipAddress, onListRule));
            return true;
        }
    }
}
