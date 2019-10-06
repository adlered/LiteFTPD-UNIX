package pers.adlered.liteftpd.graphic.update;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.graphic.main.model.MainModels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.logger.enums.Types;
import pers.adlered.liteftpd.user.status.Online;
import pers.adlered.liteftpd.user.status.bind.UserLimitBind;

import java.util.*;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>实时更新数据，并显示在界面上</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-06 22:55
 **/
public class RunningUpdate implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                List<String> list = new ArrayList<>();
                // ** Collect **
                // Online
                list.add(Dict.onlineStr() + ": " + Online.userRuleOnline.size());
                // Users
                Map<String, Integer> userMap = new HashMap<>();
                for (UserLimitBind userLimitBind : Online.userRuleOnline) {
                    String username = userLimitBind.getUsername();
                    if (userMap.get(username) != null) {
                        int count = userMap.get(username);
                        ++count;
                        userMap.put(username, count);
                    } else {
                        userMap.put(username, 1);
                    }
                }
                String users = "";
                for (Map.Entry<String, Integer> entry : userMap.entrySet()) {
                    users += entry.getKey() + ": " + entry.getValue() + " ";
                }
                list.add(users);
                // ** Update **
                String text = "";
                for (String i : list) {
                    text += i + "\n";
                }
                MainModels.data.setText(text);
                Thread.sleep(1000);
            }
        } catch (InterruptedException IE) {
            IE.printStackTrace();
        }
    }
}
