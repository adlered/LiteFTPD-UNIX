package pers.adlered.liteftpd.variable;

/**
 * Store params && settings.
 */

public class Variable {
    //User control
    public static int online = 0;
    public static long maxUserLimit = 10;
    //Time out per second
    public static int timeout = 30;
    //Service control
    public static int port = 21;
    //Info control
    public static String welcomeMessage = "LiteFTPD https://github.com/AdlerED/LiteFTPD-UNIX";
}
