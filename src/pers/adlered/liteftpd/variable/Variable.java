package pers.adlered.liteftpd.variable;

/**
 * Store params && settings.
 */

public class Variable {
    // ** User control **
    public static int online = 0;
    public static long maxUserLimit = 10;
    //Time out per second
    public static int timeout = 60;
    // ** Data control **
    //Smart choose translating encode.
    public static boolean smartEncode = false;
    //Set the default translating encode.
    public static String defaultEncode = "GB2312";
    // ** Service control **
    public static int port = 21;
    // ** Info control **
    public static String welcomeMessage = "";
}
