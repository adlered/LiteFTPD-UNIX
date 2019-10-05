package pers.adlered.liteftpd.user.status.bind;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>用户限制</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 21:02
 **/
public class UserLimitBind {
    String username;
    String rule;

    public UserLimitBind(String username, String rule) {
        this.username = username;
        this.rule = rule;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
