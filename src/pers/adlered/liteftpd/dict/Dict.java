package pers.adlered.liteftpd.dict;

import pers.adlered.liteftpd.variable.Variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Store status messages with response format.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Dict {
    public static final String passwordRequired = Code.PASSREQ + " Password required for ";
    public static final String loggedIn = Code.LOGGED + "-";
    public static final String isFile = Code.ERRTARGET + " CWD Failed to change directory to ";
    public static final String openSuccessful = Code.CORRECT + " CWD command successful. ";
    public static final String currentDir = Code.CPATH + " ";
    public static final String setType = Code.SUCCESS + " Type set to ";
    public static final String passiveMode = Code.PASSIVE + " Entering Passive Mode ";
    public static final String changeDir = Code.CORRECT + " Directory changed to ";
    public static final String openPassiveBINARY = Code.STATUSOK + " Opening BINARY mode data connection for ";
    public static final String openPassiveASCI = Code.STATUSOK + " Opening ASCII mode data connection for ";
    public static final String noSuchFileOrDir = Code.ERRTARGET + " ";
    public static final String newLine = "\r\n";
    public static final String connectionStarted = " :) " + Variable.welcomeMessage + "" + Dict.newLine + "";
    public static final String outOfOnlineLimit = Code.NOTLOGIN + " :( Too much users online." + "" + Dict.newLine + "";
    public static final String unknownCommand = Code.CMDUNKNOWN + " Command don't understood." + "" + Dict.newLine + "";
    public static final String alreadyLogged = Code.CMDUNKNOWN + " You have already logged in." + "" + Dict.newLine + "";
    public static final String bye = Code.SERVICESTOP + " :) See ya!" + "" + Dict.newLine + "";
    public static final String passiveDataFailed = Code.UNAVAILABLE + " Passive port is not connected." + Dict.newLine + "";
    public static final String openPassiveASCII = Code.STATUSOK + " Opening ASCII mode data, please wait." + Dict.newLine + "";
    public static final String type = Code.NAME + " UNIX Type: L8" + Dict.newLine + "";
    public static final String remind = "" + Dict.newLine + ">>> LiteFTPD https://github.com/AdlerED/LiteFTPD-UNIX" + Dict.newLine + "===------===" + Dict.newLine + "" +
            "IS THE CHINESE ON THE RIGHT NORMAL? -> 中文 <- If not, type \"quote gb\" to change the encode type." + Dict.newLine + "230 OK" + Dict.newLine + "";
    public static final String noPermission = "550 Sorry, but you don't have permission to do that." + Dict.newLine;
    public static final String noSuchFIleOrDir2 = ": No such file or directory." + Dict.newLine + "";
}
