package pers.adlered.liteftpd.user.info.bind;

import pers.adlered.liteftpd.user.status.bind.IpLimitBind;
import pers.adlered.liteftpd.user.status.bind.UserLimitBind;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>存储在线用户详细信息</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 21:41
 **/
public class UserInfoBind {
    private IpLimitBind ipLimitBind;
    private UserLimitBind userLimitBind;

    public UserInfoBind(IpLimitBind ipLimitBind, UserLimitBind userLimitBind) {
        this.ipLimitBind = ipLimitBind;
        this.userLimitBind = userLimitBind;
    }

    public IpLimitBind getIpLimitBind() {
        return ipLimitBind;
    }

    public void setIpLimitBind(IpLimitBind ipLimitBind) {
        this.ipLimitBind = ipLimitBind;
    }

    public UserLimitBind getUserLimitBind() {
        return userLimitBind;
    }

    public void setUserLimitBind(UserLimitBind userLimitBind) {
        this.userLimitBind = userLimitBind;
    }
}
