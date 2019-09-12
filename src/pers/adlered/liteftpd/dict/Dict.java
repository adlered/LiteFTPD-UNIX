package pers.adlered.liteftpd.dict;

import pers.adlered.liteftpd.variable.Variable;

/**
 * Store status messages with response format
 */

public class Dict {
    public static final String connectionStarted = " :) " + Variable.welcomeMessage + "\r\n";
    public static final String outOfOnlineLimit = Code.NOTLOGIN + " :( Too much users online." + "\r\n";
    public static final String unknownCommand = Code.CMDUNKNOWN + " Command don't understood." + "\r\n";
    public static final String alreadyLogged = Code.CMDUNKNOWN + " You have already logged in." + "\r\n";
    public static final String bye = Code.SERVICESTOP + " :) See ya!" + "\r\n";
    public static final String passiveDataFailed = Code.UNAVAILABLE + " Passive port is not connected.\r\n";
    public static final String openPassiveASCII = Code.STATUSOK + " Opening ASCII mode data, please wait.\r\n";
    public static final String type = Code.NAME + " UNIX Type: L8\r\n";
    public static final String remind = "\r\n>>> LiteFTPD https://github.com/AdlerED/LiteFTPD-UNIX\r\n" +
            "230 ===------===\r\n";

    public static final String passwordRequired = Code.PASSREQ + " Password required for ";
    public static final String loggedIn = Code.LOGGED + "-";
    public static final String isFile = Code.ERRTARGET + " CWD Failed to change directory to ";
    public static final String openSuccessful = Code.CORRECT + " CWD command successful. ";
    public static final String currentDir = Code.CPATH + " ";
    public static final String setType = Code.SUCCESS + " Type set to ";
    public static final String passiveMode = Code.PASSIVE + " Entering Passive Mode ";
    public static final String changeDir = Code.CORRECT + " Directory changed to ";
    public static final String openPassiveBINARY = Code.STATUSOK + " Opening BINARY mode data connection for ";
    public static final String noSuchFileOrDir = Code.ERRTARGET + " ";
    public static final String newLine = "\r\n";
    public static final String noSuchFIleOrDir2 = ": No such file or directory.\r\n";
}
