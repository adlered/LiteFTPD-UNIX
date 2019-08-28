package pers.adlered.liteftpd.bind;

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
