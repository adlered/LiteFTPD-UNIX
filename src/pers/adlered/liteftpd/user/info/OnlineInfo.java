package pers.adlered.liteftpd.user.info;

import pers.adlered.liteftpd.user.info.bind.UserInfoBind;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>存储所有在线用户详细信息的数组</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 21:47
 **/
public class OnlineInfo {
    public static final List<UserInfoBind> usersOnlineInfo = new ArrayList<>();
}
