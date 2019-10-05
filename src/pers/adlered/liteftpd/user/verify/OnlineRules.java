package pers.adlered.liteftpd.user.verify;

import pers.adlered.liteftpd.user.status.bind.IpLimitBind;
import pers.adlered.liteftpd.user.status.Online;
import pers.adlered.liteftpd.user.status.bind.UserLimitBind;
import pers.adlered.liteftpd.variable.Variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>检测连接数限制，返回True或者False决定是否接受连接。</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 14:49
 **/
public class OnlineRules {
    /**
     * 检查IP地址是否可以登录
     *
     * @param ipAddress
     * @return 返回生成后的IP Bind，用于进一步获取登录用户信息
     */
    public static IpLimitBind checkIpAddress(String ipAddress) {
        boolean onList = false;
        String onListRule = "";
        int onListLimit = -1;
        String[] ipRules = Variable.ipOnlineLimit.split(";");
        String[] ip = ipAddress.split("\\.");
        for (int i = 0; i < ipRules.length; i += 2) {
            if (ipRules[i].equals("0.0.0.0")) {
                onList = true;
                onListRule = ipRules[i];
                onListLimit = Integer.parseInt(ipRules[i + 1]);
            }
            String[] ruleIp = ipRules[i].split("\\.");
            // 首先，第一位需要相等
            if (ruleIp[0].equals(ip[0]) || ruleIp[0].toLowerCase().equals("x")) {
                if (ruleIp[1].equals(ip[1]) || ruleIp[1].toLowerCase().equals("x")) {
                    if (ruleIp[2].equals(ip[2]) || ruleIp[2].toLowerCase().equals("x")) {
                        if (ruleIp[3].equals(ip[3]) || ruleIp[3].toLowerCase().equals("x")) {
                            onList = true;
                            onListRule = ipRules[i];
                            onListLimit = Integer.parseInt(ipRules[i + 1]);
                        }
                    }
                }
            }
        }
        if (onList) {
            if (onListLimit == 0) {
                return new IpLimitBind(null, null);
            } else {
                int count = 0;
                for (IpLimitBind ipRule : Online.ipRuleOnline) {
                    if (ipRule.getRule().equals(onListRule)) {
                        ++count;
                    }
                }
                if (count < onListLimit) {
                    IpLimitBind ipLimitBind = new IpLimitBind(ipAddress, onListRule);
                    Online.ipRuleOnline.add(ipLimitBind);
                    return ipLimitBind;
                } else {
                    return new IpLimitBind(null, null);
                }
            }
        } else {
            IpLimitBind ipLimitBind = new IpLimitBind(ipAddress, onListRule);
            Online.ipRuleOnline.add(ipLimitBind);
            return ipLimitBind;
        }
    }

    /**
     * 检查用户名是否达到了上限
     *
     * @param username
     * @return true则允许登录 false则禁止登录
     */
    public static UserLimitBind checkUsername(String username) {
        boolean onList = false;
        String onListRule = "";
        int onListLimit = -1;
        String[] userRules = Variable.userOnlineLimit.split(";");
        // 先检测，再执行，通配符优先级最高
        for (int i = 0; i < userRules.length; i += 2) {
            if (userRules[i].equals("%")) {
                onList = true;
                onListRule = userRules[i];
                onListLimit = Integer.parseInt(userRules[i + 1]);
            }
            if (userRules[i].equals(username)) {
                onList = true;
                onListRule = userRules[i];
                onListLimit = Integer.parseInt(userRules[i + 1]);
            }
        }
        if (onList) {
            if (onListLimit == 0) {
                return new UserLimitBind(null, null);
            } else {
                int count = 0;
                for (UserLimitBind userRule : Online.userRuleOnline) {
                    if (userRule.getRule().equals(onListRule)) {
                        ++count;
                    }
                }
                if (count < onListLimit) {
                    UserLimitBind userLimitBind = new UserLimitBind(username, onListRule);
                    Online.userRuleOnline.add(userLimitBind);
                    return userLimitBind;
                } else {
                    return new UserLimitBind(null, null);
                }
            }
        } else {
            UserLimitBind userLimitBind = new UserLimitBind(username, onListRule);
            Online.userRuleOnline.add(userLimitBind);
            return userLimitBind;
        }
    }
}
