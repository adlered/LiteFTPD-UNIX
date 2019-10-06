package pers.adlered.liteftpd.dict;

import pers.adlered.liteftpd.tool.GoodXX;
import pers.adlered.liteftpd.variable.Variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Store status messages with response format.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Dict {
    private static String lang = "en";

    public Dict(String lang) {
        lang = lang.toLowerCase();
        this.lang = lang;
    }

    public static String gbEncodeOK(String ip) {
        if (lang.equals("en"))
            return StatusCode.SERVICEREADY + "-LiteFTPD" + Dict.newLine
                    + ">>> 编码已适应Windows FTP客户端，您现在看到的这条信息应是正常的简体中文。"
                    + Dict.newLine + ">>> Your IP address: " + ip
                    + "" + Dict.newLine + "220" + " :) " + Variable.welcomeMessage + "" + Dict.newLine;
        return null;
    }

    public static String rest(String restAt) {
        if (lang.equals("en"))
            return StatusCode.WAIT + " Restarting at " + restAt + ". Send STORE or RETRIEVE." + Dict.newLine;
        return null;
    }

    public static String noSuchFile(String path) {
        if (lang.equals("en"))
            return StatusCode.ERRTARGET + " " + path + ": No such file." + Dict.newLine;
        return null;
    }

    public static String fileSize(long size) {
        if (lang.equals("en"))
            return StatusCode.FILESTATUS + " " + size + Dict.newLine;
        return null;
    }

    public static String openAscii(String path) {
        if (lang.equals("en"))
            return StatusCode.STATUSOK + " Opening ASCII mode data connection for " + path + "." + Dict.newLine;
        return null;
    }

    public static String openBin(String path) {
        if (lang.equals("en"))
            return StatusCode.STATUSOK + " Opening BINARY mode data connection for " + path + "." + Dict.newLine;
        return null;
    }

    public static String pasvDataFailed() {
        if (lang.equals("en"))
            return StatusCode.UNAVAILABLE + " Passive port is not connected." + Dict.newLine;
        return null;
    }

    public static String openPasvAscii(String path, long fileLength) {
        if (lang.equals("en"))
            return StatusCode.STATUSOK + " Opening ASCII mode data connection for " + path + " (" + fileLength + " Bytes)" + Dict.newLine;
        return null;
    }

    public static String openPasvBin(String path, long fileLength) {
        if (lang.equals("en"))
            return StatusCode.STATUSOK + " Opening BINARY mode data connection for " + path + " (" + fileLength + " Bytes)" + Dict.newLine;
        return null;
    }

    public static String pasvMode(String[] IPADD, int calcPort, int randomSub) {
        if (lang.equals("en"))
            return StatusCode.PASSIVE + " Entering Passive Mode " + "(" + IPADD[0] + "," + IPADD[1] + "," + IPADD[2] + "," + IPADD[3] + "," + calcPort + "," + randomSub + ")" + "" + Dict.newLine;
        return null;
    }

    public static String portSuccess() {
        if (lang.equals("en"))
            return StatusCode.SUCCESS + " PORT Command successful." + Dict.newLine;
        return null;
    }

    public static String rntoSuccess() {
        if (lang.equals("en"))
            return StatusCode.CORRECT + " RNTO command successful." + Dict.newLine;
        return null;
    }

    public static String rnfrSuccess() {
        if (lang.equals("en"))
            return StatusCode.WAIT + " File or directory exists, ready for destination name." + Dict.newLine;
        return null;
    }

    public static String deleSuccess() {
        if (lang.equals("en"))
            return StatusCode.CORRECT + " DELE command successful." + Dict.newLine;
        return null;
    }

    public static String rmdSuccess() {
        if (lang.equals("en"))
            return StatusCode.CORRECT + " RMD command successful." + Dict.newLine;
        return null;
    }

    public static String createFailed(String path) {
        if (lang.equals("en"))
            return StatusCode.ERRTARGET + " " + path + ": Failed to create." + Dict.newLine;
        return null;
    }

    public static String dirCreated(String path) {
        if (lang.equals("en"))
            return StatusCode.CPATH + " \"" + path + "\" directory created." + Dict.newLine;
        return null;
    }

    public static String commandOK() {
        if (lang.equals("en"))
            return StatusCode.SUCCESS + " Command okay." + Dict.newLine;
        return null;
    }

    public static String unixType() {
        if (lang.equals("en"))
            return StatusCode.NAME + " UNIX Type: L8" + Dict.newLine;
        return null;
    }

    public static String unknownCommand() {
        if (lang.equals("en"))
            return StatusCode.CMDUNKNOWN + " Command don't understood." + Dict.newLine;
        return null;
    }

    public static String notFound(String path) {
        if (lang.equals("en"))
            return StatusCode.ERRTARGET + " " + path + ": No such file or directory." + Dict.newLine;
        return null;
    }

    public static String changeDir(String path) {
        if (lang.equals("en"))
            return StatusCode.CORRECT + " Directory changed to " + path + Dict.newLine;
        return null;
    }

    public static String list() {
        if (lang.equals("en"))
            return StatusCode.STATUSOK + " Opening ASCII mode data, please wait." + Dict.newLine;
        return null;
    }

    public static String type(String type) {
        if (lang.equals("en"))
            return StatusCode.SUCCESS + " Type set to " + type + "." + Dict.newLine;
        return null;
    }

    public static String currentDir(String path) {
        if (lang.equals("en"))
            return StatusCode.CPATH + " \"" + path + "\" is current directory." + "" + Dict.newLine;
        return null;
    }

    public static String notSupportSITE() {
        if (lang.equals("en"))
            return StatusCode.ERRSYNTAX + " SITE option not supported." + Dict.newLine;
        return null;
    }

    public static String features() {
        if (lang.equals("en"))
            return StatusCode.STATUS + "-Features:" + Dict.newLine
                    + "UTF8" + Dict.newLine
                    + StatusCode.STATUS + " End" + Dict.newLine;
        return null;
    }

    public static String alreadyLogIn() {
        if (lang.equals("en"))
            return StatusCode.CMDUNKNOWN + " You have already logged in." + "" + Dict.newLine;
        return null;
    }

    public static String wrongPassword() {
        if (lang.equals("en"))
            return StatusCode.NOTLOGIN + " Sorry, the password is wrong." + Dict.newLine;
        return null;
    }

    public static String loggedIn(String username) {
        if (lang.equals("en"))
            return StatusCode.LOGGED + "-" + "" + Dict.newLine + "===------===" + Dict.newLine + ">>> :) Good " + GoodXX.getTimeAsWord() + ", " + username + "!" + Dict.newLine
                    + ">>> LiteFTPD https://github.com/AdlerED/LiteFTPD-UNIX" + Dict.newLine + "===------===" + Dict.newLine
                    + "IS THE CHINESE ON THE RIGHT NORMAL? -> 中文 <- If not, type \"quote gb\" to change the encode type." + Dict.newLine + "230 OK" + Dict.newLine;;
        return null;
    }

    public static String tooMuchLoginInUser(String username) {
        if (lang.equals("en"))
            return StatusCode.NOTLOGIN + " Sorry, user \"" + username + "\" has too much login, please try again at later." + Dict.newLine;
        return null;
    }

    public static String utf8(boolean status) {
        if (lang.equals("en"))
            if (status)
                return StatusCode.SUCCESS + " OPTS UTF8 command successful - UTF8 encoding now ON." + Dict.newLine;
            else
                return StatusCode.SUCCESS + " OPTS UTF8 command successful - UTF8 encoding now OFF." + Dict.newLine;
        return null;
    }

    public static String bye() {
        if (lang.equals("en"))
            return StatusCode.SERVICESTOP + " :) See ya!" + "" + Dict.newLine;
        return null;
    }

    public static String passwordRequired(String username) {
        if (lang.equals("en"))
            return StatusCode.PASSREQ + " Password required for " + username + "." + "" + Dict.newLine;
        return null;
    }

    public static String permissionDenied() {
        if (lang.equals("en"))
            return StatusCode.ERRTARGET + " Permission denied." + Dict.newLine;
        return null;
    }

    public static String transferCompleteInAsciiMode(long fileLength, long time, float averageTime) {
        if (lang.equals("en"))
            return StatusCode.CLOSED + "-Transfer complete. " + fileLength + " bytes saved in " + time + " second. " + averageTime + " KB/sec." + Dict.newLine
                    + StatusCode.CLOSED + " You are using ASCII mode to transfer files. If you find that the file is corrupt, type \"binary\" and try again." + Dict.newLine;
        return null;
    }

    public static String transferComplete(long fileLength, long time, float averageTime) {
        if (lang.equals("en"))
            return StatusCode.CLOSED + " Transfer complete. " + fileLength + " bytes saved in " + time + " second. " + averageTime + " KB/sec." + Dict.newLine;
        return null;
    }

    public static String connectedMessage(String ip) {
        if (lang.equals("en"))
            return StatusCode.SERVICEREADY + "-LiteFTPD" + Dict.newLine + ">>> Please log in." + Dict.newLine + ">>> Your IP address: " + ip + Dict.newLine + "220" + " :) " + Variable.welcomeMessage + "" + Dict.newLine;
        return null;
    }

    public static String closedInReason(String reason) {
        if (lang.equals("en"))
          return "LiteFTPD > :( Sorry, the connection is closed from server! Reason: " + reason + "." + Dict.newLine;
        return null;
    }

    public static String outOfOnlineLimit() {
        if (lang.equals("en"))
            return StatusCode.NOTLOGIN + " :( Too much users online." + "" + Dict.newLine;
        return null;
    }

    public static final String newLine = "\r\n";
}
