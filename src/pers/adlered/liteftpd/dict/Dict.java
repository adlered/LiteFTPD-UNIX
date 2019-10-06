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
    private static String lang = "en_us";

    public static void init(String lang) {
        lang = lang.toLowerCase();
        Dict.lang = lang;
    }

    public static String gbEncodeOK(String ip) {
        if (lang.equals("zh_cn"))
            return  StatusCode.SERVICEREADY + "-LiteFTPD" + Dict.newLine
                    + ">>> 编码已适应Windows FTP客户端，您现在看到的这条信息应是正常的简体中文。"
                    + Dict.newLine + ">>> 你的IP地址: " + ip
                    + "" + Dict.newLine + "220" + " :) " + Variable.welcomeMessage + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.SERVICEREADY + "-LiteFTPD" + Dict.newLine
                    + ">>> 编码已适应Windows FTP客户端，您现在看到的这条信息应是正常的简体中文。"
                    + Dict.newLine + ">>> Your IP address: " + ip
                    + "" + Dict.newLine + "220" + " :) " + Variable.welcomeMessage + "" + Dict.newLine;
        return null;
    }

    public static String rest(String restAt) {
        if (lang.equals("zh_cn"))
            return StatusCode.WAIT + " 断点续传已设定于 " + restAt + ". 发送 STORE 或 RETRIEVE 以继续." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.WAIT + " Restarting at " + restAt + ". Send STORE or RETRIEVE." + Dict.newLine;
        return null;
    }

    public static String noSuchFile(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.ERRTARGET + " " + path + ": 没有这个文件." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.ERRTARGET + " " + path + ": No such file." + Dict.newLine;
        return null;
    }

    public static String fileSize(long size) {
        if (lang.equals("zh_cn"))
            return StatusCode.FILESTATUS + " " + size + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.FILESTATUS + " " + size + Dict.newLine;
        return null;
    }

    public static String openAscii(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.STATUSOK + " 为 " + path + " 开启 ASCII 模式传输数据." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.STATUSOK + " Opening ASCII mode data connection for " + path + "." + Dict.newLine;
        return null;
    }

    public static String openBin(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.STATUSOK + " 为 " + path + " 开启 BINARY 模式传输数据." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.STATUSOK + " Opening BINARY mode data connection for " + path + "." + Dict.newLine;
        return null;
    }

    public static String pasvDataFailed() {
        if (lang.equals("zh_cn"))
            return StatusCode.UNAVAILABLE + " 被动模式接口无连接." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.UNAVAILABLE + " Passive port is not connected." + Dict.newLine;
        return null;
    }

    public static String openPasvAscii(String path, long fileLength) {
        if (lang.equals("zh_cn"))
            return StatusCode.STATUSOK + " 启动 ASCII 模式数据传输： " + path + " (" + fileLength + " 字节)" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.STATUSOK + " Opening ASCII mode data connection for " + path + " (" + fileLength + " Bytes)" + Dict.newLine;
        return null;
    }

    public static String openPasvBin(String path, long fileLength) {
        if (lang.equals("zh_cn"))
            return StatusCode.STATUSOK + " 启动 BINARY 模式数据传输： " + path + " (" + fileLength + " 字节)" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.STATUSOK + " Opening BINARY mode data connection for " + path + " (" + fileLength + " Bytes)" + Dict.newLine;
        return null;
    }

    public static String pasvMode(String[] IPADD, int calcPort, int randomSub) {
        if (lang.equals("zh_cn"))
            return StatusCode.PASSIVE + " Entering Passive Mode " + "(" + IPADD[0] + "," + IPADD[1] + "," + IPADD[2] + "," + IPADD[3] + "," + calcPort + "," + randomSub + ")" + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.PASSIVE + " Entering Passive Mode " + "(" + IPADD[0] + "," + IPADD[1] + "," + IPADD[2] + "," + IPADD[3] + "," + calcPort + "," + randomSub + ")" + "" + Dict.newLine;
        return null;
    }

    public static String portSuccess() {
        if (lang.equals("zh_cn"))
            return StatusCode.SUCCESS + " PORT 命令已执行." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.SUCCESS + " PORT Command successful." + Dict.newLine;
        return null;
    }

    public static String rntoSuccess() {
        if (lang.equals("zh_cn"))
            return StatusCode.CORRECT + " RNTO 命令已执行." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CORRECT + " RNTO command successful." + Dict.newLine;
        return null;
    }

    public static String rnfrSuccess() {
        if (lang.equals("zh_cn"))
            return StatusCode.WAIT + " 文件或文件夹已选定, 请提供目标位置." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.WAIT + " File or directory exists, ready for destination name." + Dict.newLine;
        return null;
    }

    public static String deleSuccess() {
        if (lang.equals("zh_cn"))
            return StatusCode.CORRECT + " DELE 命令已执行." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CORRECT + " DELE command successful." + Dict.newLine;
        return null;
    }

    public static String rmdSuccess() {
        if (lang.equals("zh_cn"))
            return StatusCode.CORRECT + " RMD 命令已执行." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CORRECT + " RMD command successful." + Dict.newLine;
        return null;
    }

    public static String createFailed(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.ERRTARGET + " " + path + ": 创建失败." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.ERRTARGET + " " + path + ": Failed to create." + Dict.newLine;
        return null;
    }

    public static String dirCreated(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.CPATH + " 目录 \"" + path + "\" 已创建." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CPATH + " \"" + path + "\" directory created." + Dict.newLine;
        return null;
    }

    public static String commandOK() {
        if (lang.equals("zh_cn"))
            return StatusCode.SUCCESS + " 命令已执行." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.SUCCESS + " Command okay." + Dict.newLine;
        return null;
    }

    public static String unixType() {
        if (lang.equals("zh_cn"))
            return StatusCode.NAME + " UNIX Type: L8" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.NAME + " UNIX Type: L8" + Dict.newLine;
        return null;
    }

    public static String unknownCommand() {
        if (lang.equals("zh_cn"))
            return StatusCode.CMDUNKNOWN + " 未知命令." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CMDUNKNOWN + " Command don't understood." + Dict.newLine;
        return null;
    }

    public static String notFound(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.ERRTARGET + " " + path + ": 文件或文件夹不存在." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.ERRTARGET + " " + path + ": No such file or directory." + Dict.newLine;
        return null;
    }

    public static String changeDir(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.CORRECT + " 目录已更改至 " + path + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CORRECT + " Directory changed to " + path + Dict.newLine;
        return null;
    }

    public static String list() {
        if (lang.equals("zh_cn"))
            return StatusCode.STATUSOK + " 正在传输 ASCII 数据, 请稍候." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.STATUSOK + " Opening ASCII mode data, please wait." + Dict.newLine;
        return null;
    }

    public static String type(String type) {
        if (lang.equals("zh_cn"))
            return StatusCode.SUCCESS + " 传输模式已设定为 " + type + "." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.SUCCESS + " Type set to " + type + "." + Dict.newLine;
        return null;
    }

    public static String currentDir(String path) {
        if (lang.equals("zh_cn"))
            return StatusCode.CPATH + " \"" + path + "\" 是当前的目录." + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CPATH + " \"" + path + "\" is current directory." + "" + Dict.newLine;
        return null;
    }

    public static String notSupportSITE() {
        if (lang.equals("zh_cn"))
            return StatusCode.ERRSYNTAX + " SITE 选项不受支持." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.ERRSYNTAX + " SITE option not supported." + Dict.newLine;
        return null;
    }

    public static String features() {
        if (lang.equals("zh_cn"))
            return StatusCode.STATUS + "-特性:" + Dict.newLine
                    + "UTF8" + Dict.newLine
                    + StatusCode.STATUS + " 结束" + Dict.newLine;        if (lang.equals("en_us"))
            return StatusCode.STATUS + "-Features:" + Dict.newLine
                    + "UTF8" + Dict.newLine
                    + StatusCode.STATUS + " End" + Dict.newLine;
        return null;
    }

    public static String alreadyLogIn() {
        if (lang.equals("zh_cn"))
            return StatusCode.CMDUNKNOWN + " 你已经登录过了." + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CMDUNKNOWN + " You have already logged in." + "" + Dict.newLine;
        return null;
    }

    public static String wrongPassword() {
        if (lang.equals("zh_cn"))
            return StatusCode.NOTLOGIN + " 抱歉, 密码错误." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.NOTLOGIN + " Sorry, the password is wrong." + Dict.newLine;
        return null;
    }

    public static String loggedIn(String username) {
        if (lang.equals("zh_cn"))
            return StatusCode.LOGGED + "-" + Dict.newLine + "===------===" + Dict.newLine + ">>> :) Good " + GoodXX.getTimeAsWord() + ", " + username + "!" + Dict.newLine
                    + ">>> LiteFTPD https://github.com/AdlerED/LiteFTPD-UNIX" + Dict.newLine + "===------===" + Dict.newLine
                    + "右侧的中文显示是否正常? -> 中文 <- 如果乱码了, 请输入 \"quote gb\" 以修复该问题." + Dict.newLine + "230 成功" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.LOGGED + "-" + "" + Dict.newLine + "===------===" + Dict.newLine + ">>> :) Good " + GoodXX.getTimeAsWord() + ", " + username + "!" + Dict.newLine
                    + ">>> LiteFTPD https://github.com/AdlerED/LiteFTPD-UNIX" + Dict.newLine + "===------===" + Dict.newLine
                    + "IS THE CHINESE ON THE RIGHT NORMAL? -> 中文 <- If not, type \"quote gb\" to change the encode type." + Dict.newLine + "230 OK" + Dict.newLine;
        return null;
    }

    public static String tooMuchLoginInUser(String username) {
        if (lang.equals("zh_cn"))
            return StatusCode.NOTLOGIN + " 抱歉, 用户 \"" + username + "\" 连接数过多, 请稍候重试." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.NOTLOGIN + " Sorry, user \"" + username + "\" has too much login, please try again at later." + Dict.newLine;
        return null;
    }

    public static String utf8(boolean status) {
        if (lang.equals("zh_cn")) {
            if (status)
                return StatusCode.SUCCESS + " OPTS UTF8 命令已执行 - UTF8 编码现在已开启." + Dict.newLine;
            else
                return StatusCode.SUCCESS + " OPTS UTF8 命令已执行 - UTF8 编码现在已停用." + Dict.newLine;
        }
        if (lang.equals("en_us")) {
            if (status)
                return StatusCode.SUCCESS + " OPTS UTF8 command successful - UTF8 encoding now ON." + Dict.newLine;
            else
                return StatusCode.SUCCESS + " OPTS UTF8 command successful - UTF8 encoding now OFF." + Dict.newLine;
        }
        return null;
    }

    public static String bye() {
        if (lang.equals("zh_cn"))
            return StatusCode.SERVICESTOP + " :) 再见!" + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.SERVICESTOP + " :) See ya!" + "" + Dict.newLine;
        return null;
    }

    public static String passwordRequired(String username) {
        if (lang.equals("zh_cn"))
            return StatusCode.PASSREQ + " 请输入用户 " + username + " 的密码." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.PASSREQ + " Password required for " + username + "." + Dict.newLine;
        return null;
    }

    public static String permissionDenied() {
        if (lang.equals("zh_cn"))
            return StatusCode.ERRTARGET + " 没有执行此操作的权限." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.ERRTARGET + " Permission denied." + Dict.newLine;
        return null;
    }

    public static String transferCompleteInAsciiMode(long fileLength, long time, float averageTime) {
        if (lang.equals("zh_cn"))
            return StatusCode.CLOSED + "-传输完毕. " + fileLength + " 字节在 " + time + " 秒内传输完成. 平均 " + averageTime + " KB/秒." + Dict.newLine
                    + StatusCode.CLOSED + " 你正在使用 ASCII 模式传输文件. 如果你的文件是损坏的, 输入 \"binary\" 然后再重试一次." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CLOSED + "-Transfer complete. " + fileLength + " bytes saved in " + time + " second. " + averageTime + " KB/sec." + Dict.newLine
                    + StatusCode.CLOSED + " You are using ASCII mode to transfer files. If you find that the file is corrupt, type \"binary\" and try again." + Dict.newLine;
        return null;
    }

    public static String transferComplete(long fileLength, long time, float averageTime) {
        if (lang.equals("zh_cn"))
            return StatusCode.CLOSED + " 传输完毕. " + fileLength + " 字节在 " + time + " 秒内传输完毕. 平均 " + averageTime + " KB/秒." + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.CLOSED + " Transfer complete. " + fileLength + " bytes saved in " + time + " second. " + averageTime + " KB/sec." + Dict.newLine;
        return null;
    }

    public static String connectedMessage(String ip) {
        if (lang.equals("zh_cn"))
            return StatusCode.SERVICEREADY + "-LiteFTPD" + Dict.newLine + ">>> 请登录." + Dict.newLine + ">>> 你的IP地址: " + ip + Dict.newLine + "220" + " :) " + Variable.welcomeMessage + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.SERVICEREADY + "-LiteFTPD" + Dict.newLine + ">>> Please log in." + Dict.newLine + ">>> Your IP address: " + ip + Dict.newLine + "220" + " :) " + Variable.welcomeMessage + "" + Dict.newLine;
        return null;
    }

    public static String closedInReason(String reason) {
        if (lang.equals("zh_cn"))
            return "LiteFTPD > :( 抱歉, 服务端已经关闭了连接! 原因: " + reason + "." + Dict.newLine;
        if (lang.equals("en_us"))
            return "LiteFTPD > :( Sorry, the connection is closed from server! Reason: " + reason + "." + Dict.newLine;
        return null;
    }

    public static String outOfOnlineLimit() {
        if (lang.equals("zh_cn"))
            return StatusCode.NOTLOGIN + " :( 在线用户数过多." + "" + Dict.newLine;
        if (lang.equals("en_us"))
            return StatusCode.NOTLOGIN + " :( Too much users online." + "" + Dict.newLine;
        return null;
    }

    public static final String newLine = "\r\n";
}
