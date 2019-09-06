package pers.adlered.liteftpd.variable;

/**
 * Store params && settings.
 */

public class Variable {
    // ** User control **
    public static int online = 0;
    //Set to 0, will be ignore the limit.
    public static long maxUserLimit = 0;
    //Time out per second
    public static int timeout = 100;
    // ** Data control **
    //Smart choose translating encode.
    public static boolean smartEncode = true;
    //Set the default translating encode.
    public static String defaultEncode = "GB2312";
    // ** Service control **
    public static int port = 21;
    // ** Info control **
    public static String welcomeMessage = "";
    // ** Appoint passive mode port range **
    //Recommend 100+ ports in the range to make sure generation have high-performance
    public static int minPort = 1024;
    public static int maxPort = 10240;
}
