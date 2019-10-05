package pers.adlered.liteftpd.user;

import pers.adlered.liteftpd.variable.Variable;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Store users.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class User {
    private static Map<String, UserProps> users = null;

    //  读取用户配置，并写入到users中。启动时必须配置！
    public static void initUsers() {
        users = new HashMap<>();
        String[] userList = Variable.user.split(";");
        for (int i = 0; i < userList.length; i += 5) {
            UserProps userProps = new UserProps();
            userProps.setPassword(userList[i + 1]);
            userProps.setPermission(userList[i + 2]);
            userProps.setPermitDir(userList[i + 3]);
            userProps.setDefaultDir(userList[i + 4]);
            users.put(userList[i], userProps);
        }
    }

    public static boolean checkPassword(String username, String password) {
        try {
            if (users.get(username).getPassword().equals(password)) {
                return true;
            }
        } catch (NullPointerException NPE) {
            return false;
        }
        return false;
    }

    public static UserProps getUserProps(String username) {
        return users.get(username);
    }
}
