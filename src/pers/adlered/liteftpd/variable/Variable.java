package pers.adlered.liteftpd.variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>User interface settings.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Variable {
    // ** System control **
    /* Debug level
    Too high level will be in performance trouble!
    0: NONE;
    1: INFO;
    2: WARN && INFO;
    3: ERROR && WARN && INFO;
    4: DEBUG && ERROR && WARN && INFO;
     */
    public static int debugLevel = 4;
    // ** User control **
    public static int online = 0;
    //Set to 0, will be ignore the limit. Too small value may make multi-thread ftp client not working.
    public static long maxUserLimit = 100;
    //Timeout in second
    public static int timeout = 100;
    //On mode timeout when client is on passive or initiative mode (default: 21600 sec = 6 hrs)
    public static int maxTimeout = 21600;
    // ** Data control **
    //Smart choose translating encode.
    public static boolean smartEncode = true;
    //Set the default translating encode. Unix is UTF-8, Windows is GB2312.
    public static String defaultEncode = "UTF-8";
    // ** Service control **
    public static int port = 21;
    // ** Info control **
    public static String welcomeMessage = "This is a demo version.";
    // ** Appoint passive mode port range **
    //Recommend 100+ ports in the range to make sure generation have high-performance
    public static int minPort = 10240;
    public static int maxPort = 20480;
}
