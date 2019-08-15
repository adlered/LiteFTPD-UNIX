package pers.adlered.liteftpd.dict;

import pers.adlered.liteftpd.variable.Variable;

/**
 * Store status messages with response format
 */

public class Dict {
    public static final String connectionStarted = Code.SERVICEREADY + " " + Variable.welcomeMessage + "\r\n";
    public static final String outOfOnlineLimit = Code.NOTLOGIN + " Too much users online." + "\r\n";
    public static final String unknownCommand = Code.CMDUNKNOWN + " Command don't understood." + "\r\n";
    public static final String alreadyLogged = Code.CMDUNKNOWN + " You have already logged in." + "\r\n";
    public static final String bye = Code.SERVICESTOP + " See ya!" + "\r\n";

    public static final String passwordRequired = Code.PASSREQ + " Password required for ";
    public static final String loggedIn = Code.LOGGED + " User ";
    public static final String isFile = Code.ERRTARGET + " CWD Failed to change directory to ";
    public static final String openSuccessful = Code.CORRECT + " CWD command successful. ";
    public static final String currentDir = Code.CPATH + " ";
    public static final String setType = Code.SUCCESS + " Type set to ";
    public static final String passiveMode = Code.PASSIVE + " Entering Passive Mode ";
}
