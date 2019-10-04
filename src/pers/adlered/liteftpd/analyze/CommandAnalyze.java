package pers.adlered.liteftpd.analyze;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Code;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;
import pers.adlered.liteftpd.main.PauseListen;
import pers.adlered.liteftpd.main.Send;
import pers.adlered.liteftpd.mode.PASV;
import pers.adlered.liteftpd.mode.PORT;
import pers.adlered.liteftpd.tool.GoodXX;
import pers.adlered.liteftpd.tool.RandomNum;
import pers.adlered.liteftpd.user.Permission;
import pers.adlered.liteftpd.variable.Variable;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>To analyze user input into command, and execute it.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class CommandAnalyze {
    /**
     * Step 1: Required username;
     * Step 2: Required password;
     * Step 3: Logged in.
     */
    private int step = 1;

    private String loginUser = null;
    private String loginPass = null;
    private String SRVIPADD = null;

    private Send send = null;

    private File file = null;
    private PASV passiveMode = null;
    private PORT portMode = null;
    private String mode = null;

    private String currentPath = "";
    private String lockPath = "";
    private String RNFR = null;
    //Trans type default A
    //A: ASCII I: BINARY
    private String type = "A";
    //To change timeout when command input
    private PauseListen pauseListen = null;
    private IPAddressBind ipAddressBind = null;

    private PrivateVariable privateVariable = null;

    public CommandAnalyze(Send send, String SRVIPADD, PrivateVariable privateVariable, PauseListen pauseListen, IPAddressBind ipAddressBind) {
        this.send = send;
        this.SRVIPADD = SRVIPADD;
        currentPath = Permission.defaultDir;
        this.privateVariable = privateVariable;
        this.pauseListen = pauseListen;
        this.ipAddressBind = ipAddressBind;
    }

    public void analyze(String command) {
        pauseListen.resetTimeout();
        String cmd = null;
        String arg1 = null;
        String arg2 = null;
        String[] split = null;
        try {
            split = command.split(" ");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].replaceAll("(\r|\n)", "");
            }
            cmd = split[0];
            if (split.length == 2) {
                arg1 = split[1];
            }
            else if (split.length > 2) {
                arg1 = split[1];
                arg2 = split[2];
            }
        } catch (Exception E) {
            //TODO
            E.printStackTrace();
        }
        if (cmd != null) {
            cmd = cmd.toUpperCase();
            switch (step) {
                case 1:
                    if (cmd.equals("USER")) {
                        if (arg1 == null) {
                            arg1 = "anonymous";
                        }
                        Logger.log(Types.SYS, Levels.DEBUG,Thread.currentThread() + " User login: " + arg1);
                        loginUser = arg1;
                        send.send(Dict.passwordRequired + loginUser + "." + "\r\n");
                        step = 2;
                    }
                    else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye);
                        privateVariable.setInterrupted(true);
                    }
                    else if (cmd.equals("OPTS")) {
                        if (arg1 != null) {
                            arg1 = arg1.toUpperCase();
                            if (arg1.equals("UTF8")) {
                                if (arg2 != null) {
                                    arg2 = arg2.toUpperCase();
                                    if (arg2.equals("ON")) {
                                        privateVariable.setEncode("UTF-8");
                                        privateVariable.setEncodeLock(true);
                                        send.send("200 OPTS UTF8 command successful - UTF8 encoding now ON." + Dict.newLine);
                                    } else if (arg2.equals("OFF")) {
                                        privateVariable.setEncode(Variable.defaultEncode);
                                        privateVariable.setEncodeLock(true);
                                        send.send("200 OPTS UTF8 command successful - UTF8 encoding now OFF." + Dict.newLine);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        unknownCommand();
                    }
                    break;
                case 2:
                    if (cmd.equals("PASS")) {
                        Logger.log(Types.SYS, Levels.DEBUG,"User " + loginUser + "'s password: " + arg1);
                        loginPass = arg1;
                        send.send(Dict.loggedIn + "\r\n===------===\r\n>>> :) Good " + GoodXX.getTimeAsWord() + ", " + loginUser + "!" + Dict.remind);
                        Logger.log(Types.SYS, Levels.INFO,"User " + loginUser + " logged in.");
                        step = 3;
                    }
                    else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye);
                        privateVariable.setInterrupted(true);
                    }
                    else {
                        unknownCommand();
                    }
                    break;
                case 3:
                    if (cmd.equals("USER") || cmd.equals("PASS")) {
                        send.send(Dict.alreadyLogged);
                    }
                    /**
                     * INFO COMMANDS
                     */
                    else if (cmd.equals("FEAT")) {
                        send.send("211-Features:\r\n" +
                                "UTF8\r\n" +
                                "211 End\r\n");
                    }
                    else if (cmd.equals("SITE")) {
                        send.send("501 SITE option not supported." + Dict.newLine);
                    }
                    /**
                     * NORMAL COMMANDS
                     */
                    else if (cmd.equals("PWD")) {
                        //if (file.isDirectory()) {
                            send.send(Dict.currentDir + "\"" + getLockPath(currentPath, Permission.defaultDir) + "\" is current directory." + "\r\n");
                        //} else {
                        //    send.send(Dict.isFile + file.getName() + "\r\n");
                        //}
                    }
                    else if (cmd.equals("TYPE")) {
                        arg1 = arg1.toUpperCase();
                        switch (arg1) {
                            case "I":
                                type = "I";
                                send.send(Dict.setType + "I." + "\r\n");
                                break;
                            case "A":
                                type = "A";
                                send.send(Dict.setType + "A." + "\r\n");
                                break;
                        }
                    }
                    else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye);
                        privateVariable.setInterrupted(true);
                    }
                    else if (cmd.equals("LIST")) {
                        send.send(Dict.openPassiveASCII);
                        privateVariable.setTimeoutLock(true);
                        try {
                            Process process = Runtime.getRuntime().exec(new String[]{"ls", "-l", currentPath});
                            process.waitFor();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            StringBuilder result = new StringBuilder();
                            while ((line = bufferedReader.readLine()) != null) {
                                result.append(line).append('\n');
                            }
                            if (mode != null) {
                                Logger.log(Types.TRANS, Levels.DEBUG, "Reset mode.");
                                String mode = this.mode;
                                this.mode = null;
                                Logger.log(Types.TRANS, Levels.DEBUG, "Hello " + mode + " mode.");
                                switch (mode) {
                                    case "port":
                                        portMode.hello(result.toString());
                                        portMode.start();
                                        break;
                                    case "passive":
                                        passiveMode.hello(result.toString());
                                        break;
                                }
                            }
                        } catch (IOException IOE) {
                            //TODO
                            IOE.printStackTrace();
                        } catch (InterruptedException IE) {
                            //TODO
                            IE.printStackTrace();
                        }
                    }
                    else if (cmd.equals("CDUP")) {
                        upperDirectory();
                        send.send("250 Directory changed to " + getLockPath(currentPath, Permission.defaultDir) + "\r\n");
                    }
                    else if (cmd.equals("CWD")) {
                        if (arg1 != null) {
                            String completePath = arg1;
                            if (arg2 != null) {
                                for (int i = 2; i < split.length; i++) {
                                    //Make "/Users/$" to "/Users$"
                                    if (i == split.length - 1) {
                                        split[i] = split[i].replaceAll("/$", "");
                                    }
                                    //Add
                                    completePath += " " + split[i];
                                }
                            }
                            if (completePath.equals("..")) {
                                upperDirectory();
                                send.send(Dict.changeDir + getLockPath(currentPath, Permission.defaultDir) + Dict.newLine);
                            } else {
                                if (completePath.indexOf("../") != -1) {
                                    completePath = completePath.replaceAll("\\.\\./", "");
                                }
                                completePath = getAbsolutePath(completePath);
                                File file = new File(completePath);
                                if (file.exists()) {
                                    if (file.isFile()) {
                                        send.send("550 " + getLockPath(completePath, Permission.defaultDir) + ": No such file or directory." + Dict.newLine);
                                    } else {
                                        currentPath = completePath;
                                        send.send(Dict.changeDir + getLockPath(currentPath, Permission.defaultDir) + Dict.newLine);
                                    }
                                } else {
                                    send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                                }
                            }
                        } else {
                            send.send(Dict.unknownCommand);
                        }
                    }
                    else if (cmd.equals("SYST")) {
                        send.send(Dict.type);
                    }
                    else if (cmd.equals("NOOP")) {
                        privateVariable.setTimeoutLock(true);
                        send.send("200 Command okay." + Dict.newLine);
                    }
                    else if (cmd.equals("MKD")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        File file = new File(completePath);
                        if (!file.exists()) {
                            file.mkdirs();
                            send.send("257 \"" + getLockPath(completePath, Permission.defaultDir) + "\" directory created." + Dict.newLine);
                        } else {
                            send.send("550 " + getLockPath(completePath, Permission.defaultDir) + ": Failed to create." + Dict.newLine);
                        }
                    } else if (cmd.equals("RMD")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        File file = new File(completePath);
                        if (file.isDirectory()) {
                            delFolder(completePath);
                            send.send("250 RMD command successful." + Dict.newLine);
                        } else {
                            send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                        }
                    } else if (cmd.equals("DELE")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        File file = new File(completePath);
                        if (file.isFile()) {
                            file.delete();
                            send.send("250 DELE command successful." + Dict.newLine);
                        } else {
                            send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                        }
                    }
                    else if (cmd.equals("RNFR")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        File file = new File(completePath);
                        if (file.exists()) {
                            RNFR = completePath;
                            send.send("350 File or directory exists, ready for destination name" + Dict.newLine);
                        } else {
                            send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                        }
                    }
                    else if (cmd.equals("RNTO")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        File file = new File(RNFR);
                        if (file.renameTo(new File(completePath))) {
                            send.send("250 RNTO command successful." + Dict.newLine);
                        } else {
                            send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                        }
                        RNFR = null;
                    }
                    /**
                     * TRANSMISSION COMMANDS
                     */
                    else if (cmd.equals("PORT")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        String[] analyzeStep1 = completePath.split(",");
                        int[] analyzeStep2 = new int[analyzeStep1.length];
                        for (int i = 0; i < analyzeStep1.length; i++) {
                            analyzeStep2[i] = Integer.parseInt(analyzeStep1[i]);
                        }
                        String ip = "";
                        int port = -1;
                        try {
                            for (int i = 0; i < 4; i++) {
                                if (i < 3) {
                                    ip += analyzeStep2[i] + ".";
                                } else {
                                    ip += analyzeStep2[i];
                                }
                            }
                            port = (analyzeStep2[4] * 256) + analyzeStep2[5];
                        } catch (ArrayIndexOutOfBoundsException AIOOBE) {
                            AIOOBE.printStackTrace();
                        }
                        if (portMode != null) {
                            portMode.stopSocket();
                        }
                        portMode = new PORT(send, privateVariable, pauseListen);
                        portMode.setTarget(ip, port);
                        send.send("200 PORT Command successful.\r\n");
                        mode = "port";
                    }
                    else if (cmd.equals("PASV")) {
                        if (passiveMode != null) {
                            passiveMode.stopSocket();
                        }
                        passiveMode = new PASV(send, privateVariable, pauseListen);
                        int randomPort;
                        int randomSub;
                        int calcPort;
                        int finalPort;
                        do {
                            Map<String, Integer> map = generatePort();
                            randomPort = map.get("randomPort");
                            randomSub = map.get("randomSub");
                            calcPort = map.get("calcPort");
                            finalPort = map.get("finalPort");
                        } while (!passiveMode.listen(finalPort));
                        String[] IPADD = (SRVIPADD.split(":")[0]).split("\\.");
                        send.send(Dict.passiveMode + "(" + IPADD[0] + "," + IPADD[1] + "," + IPADD[2] + "," + IPADD[3] + "," + calcPort + "," + randomSub + ")" + "\r\n");
                        passiveMode.start();
                        mode = "passive";
                    }
                    else if (cmd.equals("RETR")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        try {
                            File file = new File(completePath);
                            if (file.exists()) {
                                send.send(Dict.openPassiveBINARY + getLockPath(completePath, Permission.defaultDir) + " (" + file.length() + " Bytes)\r\n");
                                if (mode != null) {
                                    Logger.log(Types.TRANS, Levels.DEBUG, "Reset mode.");
                                    String mode = this.mode;
                                    this.mode = null;
                                    Logger.log(Types.TRANS, Levels.DEBUG, "Hello " + mode + " mode.");
                                    switch (mode) {
                                        case "port":
                                            portMode.hello(file);
                                            portMode.start();
                                            break;
                                        case "passive":
                                            passiveMode.hello(file);
                                            break;
                                    }
                                }
                            } else {
                                send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                            }
                        } catch (NullPointerException NPE) {
                            send.send(Dict.passiveDataFailed);
                        }
                    }
                    else if (cmd.equals("STOR")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        if (completePath.indexOf("../") != -1) {
                            completePath = completePath.replaceAll("\\.\\./", "");
                        }
                        completePath = getAbsolutePath(completePath);
                        send.send("150 Opening BINARY mode data connection for " + getLockPath(completePath, Permission.defaultDir) + "." + Dict.newLine);
                        try {
                            /*File file = new File(completePath);
                            if (file.exists()) {
                                send.send(Dict.openPassiveBINARY + getLockPath(completePath, Permission.defaultDir) + " (" + file.length() + " Bytes)\r\n");
                                passiveMode.hello(file);
                            } else {
                                send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
                            }*/
                            if (mode != null) {
                                Logger.log(Types.TRANS, Levels.DEBUG, "Reset mode.");
                                String mode = this.mode;
                                this.mode = null;
                                Logger.log(Types.TRANS, Levels.DEBUG, "Hello " + mode + " mode.");
                                switch (mode) {
                                    case "port":
                                        portMode.helloSTOR(completePath);
                                        portMode.start();
                                        break;
                                    case "passive":
                                        passiveMode.helloSTOR(completePath);
                                        break;
                                }
                            }
                        } catch (NullPointerException NPE) {
                            send.send(Dict.passiveDataFailed);
                        }
                    }
                    else if (cmd.equals("SIZE")) {
                        String completePath = arg1;
                        if (arg2 != null) {
                            for (int i = 2; i < split.length; i++) {
                                completePath += " " + split[i];
                            }
                        }
                        completePath = getAbsolutePath(completePath);
                        File file = new File(completePath);
                        if (file.exists() && file.isFile()) {
                            send.send("213 " + file.length() + Dict.newLine);
                        } else {
                            send.send("550 " + completePath + ": No such file." + Dict.newLine);
                        }
                    }
                    else if (cmd.equals("OPTS")) {
                        if (arg1 != null) {
                            arg1 = arg1.toUpperCase();
                            if (arg1.equals("UTF8")) {
                                if (arg2 != null) {
                                    arg2 = arg2.toUpperCase();
                                    if (arg2.equals("ON")) {
                                        privateVariable.setEncode("UTF-8");
                                        privateVariable.setEncodeLock(true);
                                        send.send("200 OPTS UTF8 command successful - UTF8 encoding now ON." + Dict.newLine);
                                    } else if (arg2.equals("OFF")) {
                                        privateVariable.setEncode(Variable.defaultEncode);
                                        privateVariable.setEncodeLock(true);
                                        send.send("200 OPTS UTF8 command successful - UTF8 encoding now OFF." + Dict.newLine);
                                    }
                                }
                            }
                        }
                    }
                    else if (cmd.equals("REST")) {
                        if (arg1 != null) {
                            send.send("350 Restarting at " + arg1 + ". Send STORE or RETRIEVE." + Dict.newLine);
                            try {
                                privateVariable.setRest(Long.parseLong(arg1));
                            } catch (Exception E) {
                                E.printStackTrace();
                            }
                        }
                    }
                    else if (cmd.equals("ABOR")) {
                        send.send(Dict.bye);
                        privateVariable.setInterrupted(true);
                    }
                    else if (cmd.equals("GB")) {
                        privateVariable.setEncode("GB2312");
                        privateVariable.setEncodeLock(true);
                        send.send(Code.SERVICEREADY + "-LiteFTPD\r\n>>> 编码已适应Windows FTP客户端，您现在看到的这条信息应是正常的简体中文。\r\n>>> Your IP address: " + ipAddressBind.getIPADD() + "\r\n220" + Dict.connectionStarted);
                    }
                    else {
                        unknownCommand();
                    }
                    break;
            }
        }
    }

    public boolean unknownCommand() {
        send.send(Dict.unknownCommand);
        return true;
    }

    /*
    根据绝对目录和锁定目录，计算相对目录
     */
    public String getLockPath(String absolutePath, String lockPath) {
        String resolve = absolutePath.replaceAll("^(" + lockPath + ")", "");
        if (resolve.isEmpty()) resolve = "/";
        if (!resolve.startsWith("/")) resolve = "/" + resolve;
        return resolve;
    }

    public void upperDirectory() {
        //Depart && Re-part path
        if (!currentPath.equals("/")) {
            String[] dir = currentPath.split("/");
            currentPath = "";
            for (int i = 0; i < dir.length - 1; i++) {
                Logger.log(Types.SYS, Levels.DEBUG,"len: " + dir.length + " cur: " + i);
                if (i == dir.length - 2) {
                    currentPath += dir[i];
                } else {
                    currentPath += dir[i] + "/";
                }
            }
            if (currentPath.isEmpty()) currentPath = "/";
        }
        Logger.log(Types.SYS, Levels.DEBUG, currentPath);
    }

    /*
    根据CWD定位的目录，计算绝对目录的位置。
     */
    @SuppressWarnings("deprecation")
    public String getAbsolutePath(String path) {
        //path = URLDecoder.decode(path);
        if (path.matches("^(./).*")) {
            path = path.replaceAll("^(./)", "");
            //Not totally equals "./"
            if (path.isEmpty()) {
                path = currentPath;
            } else {
                path = currentPath + "/" + path;
            }
        } else if (path.matches("^(/).*")) {
            path = path.replaceAll("^(/)", "");
            if (path.isEmpty()) {
                path = Permission.defaultDir;
            } else {
                path = Permission.defaultDir + "/" + path;
            }
        } else {
            path = currentPath + "/" + path;
        }
        path = path.replaceAll("//", "/");
        Logger.log(Types.SYS, Levels.DEBUG,"Absolute path: " + path);
        return path;
    }

    public static boolean isPortUsing(String host,int port) {
        boolean flag = false;
        try {
            Socket socket = new Socket(host, port);
            flag = true;
            socket.close();
        } catch (IOException IOE) {
            Logger.log(Types.SYS, Levels.DEBUG,"Port " + port + " already in use, re-generating...");
        }
        return flag;
    }

    public Map<String, Integer> generatePort() {
        Map<String, Integer> map = new HashMap();
        int randomPort;
        int randomSub;
        int calcPort;
        int finalPort;
        int count = 0;
        do {
            randomPort = RandomNum.sumIntger(Variable.minPort, Variable.maxPort, false);
            randomSub = RandomNum.sumIntger(0, 64, false);
            calcPort = (randomPort - randomSub) / 256;
            finalPort = calcPort * 256 + randomSub;
            ++count;
        } while (finalPort < Variable.minPort || finalPort > Variable.maxPort || randomSub < 0 || randomSub > 64);
        Logger.log(Types.SYS, Levels.DEBUG,count + " times while generating port: " + finalPort);
        map.put("randomPort", randomPort);
        map.put("randomSub", randomSub);
        map.put("calcPort", calcPort);
        map.put("finalPort", finalPort);
        return map;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
