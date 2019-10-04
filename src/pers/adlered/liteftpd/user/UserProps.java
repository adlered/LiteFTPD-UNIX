package pers.adlered.liteftpd.user;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>用户信息</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-04 19:16
 **/
public class UserProps {
    private String password;
    private String permission;
    private String permitDir;
    private String defaultDir;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermitDir() {
        return permitDir;
    }

    public void setPermitDir(String permitDir) {
        this.permitDir = permitDir;
    }

    public String getDefaultDir() {
        return defaultDir;
    }

    public void setDefaultDir(String defaultDir) {
        this.defaultDir = defaultDir;
    }
}
