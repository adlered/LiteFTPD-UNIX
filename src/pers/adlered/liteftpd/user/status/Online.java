package pers.adlered.liteftpd.user.status;

import pers.adlered.liteftpd.user.status.bind.IpLimitBind;
import pers.adlered.liteftpd.user.status.bind.UserLimitBind;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>记录在线的用户信息，用于限制连接数和读取信息。</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 14:51
 **/
public class Online {
    // 存储指定规则的在线IP数量
    public static final List<IpLimitBind> ipRuleOnline = new ArrayList<>();
    // 存储制定规则的在线用户数量
    public static final List<UserLimitBind> userRuleOnline = new ArrayList<>();
}
