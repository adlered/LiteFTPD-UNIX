package pers.adlered.liteftpd.user.status.bind;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>IP地址限制</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-05 15:14
 **/
public class IpLimitBind {
    String ip;
    String rule;

    public IpLimitBind(String ip, String rule) {
        this.ip = ip;
        this.rule = rule;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
