package pers.adlered.liteftpd.variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>User interface settings.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Variable {
    public static String user = "1;anonymous;;r;2;admin;123456;r";
    public static String ipOnlineLimit = "";
    public static String userOnlineLimit = "";
    public static String speedLimit="";
    public static int debugLevel = 4;
    public static int online = 0;
    public static long maxUserLimit = 100;
    public static int timeout = 100;
    public static int maxTimeout = 21600;
    public static boolean smartEncode = true;
    public static String defaultEncode = "UTF-8";
    public static int port = 21;
    public static String welcomeMessage = "";
    public static int minPort = 10240;
    public static int maxPort = 20480;
}
