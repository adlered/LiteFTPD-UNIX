package pers.adlered.liteftpd.user.status.bind;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>A bind of server/client IP Address.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class IPAddressBind {
    private String IPADD;
    private String SRVIPADD;

    public IPAddressBind(String IPADD, String SRVIPADD) {
        this.IPADD = IPADD;
        this.SRVIPADD = SRVIPADD;
    }

    public String getIPADD() {
        return IPADD;
    }

    public void setIPADD(String IPADD) {
        this.IPADD = IPADD;
    }

    public String getSRVIPADD() {
        return SRVIPADD;
    }

    public void setSRVIPADD(String SRVIPADD) {
        this.SRVIPADD = SRVIPADD;
    }
}
