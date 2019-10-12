package pers.adlered.liteftpd.analyze;

import pers.adlered.liteftpd.user.status.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.StatusCode;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.enums.Types;
import pers.adlered.liteftpd.wizard.init.PauseListen;
import pers.adlered.liteftpd.wizard.init.Send;
import pers.adlered.liteftpd.mode.PASV;
import pers.adlered.liteftpd.mode.PORT;
import pers.adlered.liteftpd.tool.GoodXX;
import pers.adlered.liteftpd.tool.RandomNum;
import pers.adlered.liteftpd.user.User;
import pers.adlered.liteftpd.user.UserProps;
import pers.adlered.liteftpd.user.info.OnlineInfo;
import pers.adlered.liteftpd.user.info.bind.UserInfoBind;
import pers.adlered.liteftpd.user.status.bind.IpLimitBind;
import pers.adlered.liteftpd.user.status.bind.UserLimitBind;
import pers.adlered.liteftpd.user.verify.OnlineRules;
import pers.adlered.liteftpd.variable.OnlineUserController;
import pers.adlered.liteftpd.variable.Variable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

    private String currentPath = null;
    private String lockPath = null;
    private String RNFR = null;
    //Trans type default A
    //A: ASCII I: BINARY
    private String type = "A";
    //To change timeout when command input
    private PauseListen pauseListen = null;
    private IPAddressBind ipAddressBind = null;

    private PrivateVariable privateVariable = null;
    private UserProps userProps = null;

    private IpLimitBind ipLimitBind = null;
    private UserLimitBind userLimitBind = null;

    public CommandAnalyze(Send send, String SRVIPADD, PrivateVariable privateVariable, PauseListen pauseListen, IPAddressBind ipAddressBind, IpLimitBind ipLimitBind) {
        this.send = send;
        this.SRVIPADD = SRVIPADD;
        this.privateVariable = privateVariable;
        this.pauseListen = pauseListen;
        this.ipAddressBind = ipAddressBind;
        this.ipLimitBind = ipLimitBind;
    }

    public static boolean isPortUsing(String host, int port) {
        boolean flag = false;
        try {
            Socket socket = new Socket(host, port);
            flag = true;
            socket.close();
        } catch (IOException IOE) {
            Logger.log(Types.SYS, Levels.DEBUG, "Port " + port + " already in use, re-generating...");
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath;
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
            } else if (split.length > 2) {
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
                        privateVariable.setUsername(arg1);
                        Logger.log(Types.SYS, Levels.DEBUG, Thread.currentThread() + " User login: " + arg1);
                        loginUser = arg1;
                        send.send(Dict.passwordRequired(loginUser));
                        step = 2;
                    } else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye());
                        privateVariable.setInterrupted(true);
                    } else if (cmd.equals("OPTS")) {
                        if (arg1 != null) {
                            arg1 = arg1.toUpperCase();
                            if (arg1.equals("UTF8")) {
                                if (arg2 != null) {
                                    arg2 = arg2.toUpperCase();
                                    if (arg2.equals("ON")) {
                                        privateVariable.setEncode("UTF-8");
                                        privateVariable.setEncodeLock(true);
                                        send.send(Dict.utf8(true));
                                    } else if (arg2.equals("OFF")) {
                                        privateVariable.setEncode(Variable.defaultEncode);
                                        privateVariable.setEncodeLock(true);
                                        send.send(Dict.utf8(false));
                                    }
                                }
                            }
                        }
                    } else {
                        unknownCommand();
                    }
                    break;
                case 2:
                    if (cmd.equals("PASS")) {
                        Logger.log(Types.SYS, Levels.DEBUG, "User " + loginUser + "'s password: " + arg1);
                        loginPass = arg1;
                        userLimitBind = OnlineRules.checkUsername(loginUser);
                        if (userLimitBind.getUsername() == null) {
                            send.send(Dict.tooMuchLoginInUser(loginUser));
                            privateVariable.reason = "user \"" + loginUser + "\" has too much login";
                            privateVariable.setInterrupted(true);
                        } else {
                            if (User.checkPassword(loginUser, loginPass) || loginUser.equals("anonymous")) {
                                OnlineInfo.usersOnlineInfo.add(new UserInfoBind(ipLimitBind, userLimitBind));
                                send.send(Dict.loggedIn(loginUser));
                                Logger.log(Types.SYS, Levels.INFO, "User " + loginUser + " logged in.");
                                userProps = User.getUserProps(loginUser);
                                lockPath = userProps.getPermitDir();
                                currentPath = userProps.getDefaultDir();
                                OnlineUserController.printOnline();
                                step = 3;
                            } else {
                                send.send(Dict.wrongPassword());
                                privateVariable.setInterrupted(true);
                            }
                        }
                    } else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye());
                        privateVariable.setInterrupted(true);
                    } else {
                        unknownCommand();
                    }
                    break;
                case 3:
                    if (cmd.equals("USER") || cmd.equals("PASS")) {
                        send.send(Dict.alreadyLogIn());
                    }
                    /**
                     * INFO COMMANDS
                     */
                    else if (cmd.equals("FEAT")) {
                        send.send(Dict.features());
                    } else if (cmd.equals("SITE")) {
                        send.send(Dict.notSupportSITE());
                    }
                    /**
                     * NORMAL COMMANDS
                     */
                    else if (cmd.equals("PWD")) {
                        //if (file.isDirectory()) {
                        send.send(Dict.currentDir(getLockPath(currentPath, lockPath)));
                        //} else {
                        //    send.send(Dict.isFile + file.getName() + "" + Dict.newLine);
                        //}
                    } else if (cmd.equals("TYPE")) {
                        arg1 = arg1.toUpperCase();
                        type = arg1;
                        send.send(Dict.type(arg1));
                    } else if (cmd.equals("BINARY")) {
                        type = "I";
                        send.send(Dict.type("I"));
                    } else if (cmd.equals("ASCII")) {
                        type = "A";
                        send.send(Dict.type("A"));
                    } else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye());
                        privateVariable.setInterrupted(true);
                    } else if (cmd.equals("LIST")) {
                        send.send(Dict.list());
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
                    } else if (cmd.equals("CDUP")) {
                        upperDirectory();
                        send.send(Dict.changeDir(getLockPath(currentPath, lockPath)));
                    } else if (cmd.equals("CWD")) {
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
                                send.send(Dict.changeDir(getLockPath(currentPath, lockPath)));
                            } else {
                                if (completePath.indexOf("../") != -1) {
                                    completePath = completePath.replaceAll("\\.\\./", "");
                                }
                                completePath = getAbsolutePath(completePath);
                                File file = new File(completePath);
                                if (file.exists()) {
                                    if (file.isFile()) {
                                        send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                                    } else {
                                        currentPath = completePath;
                                        send.send(Dict.changeDir(getLockPath(currentPath, lockPath)));
                                    }
                                } else {
                                    send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                                }
                            }
                        } else {
                            send.send(Dict.unknownCommand());
                        }
                    } else if (cmd.equals("SYST")) {
                        send.send(Dict.unixType());
                    } else if (cmd.equals("NOOP")) {
                        privateVariable.setTimeoutLock(true);
                        send.send(Dict.commandOK());
                    } else if (cmd.equals("MKD")) {
                        if (userProps.getPermission().contains("c")) {
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
                                send.send(Dict.dirCreated(getLockPath(completePath, lockPath)));
                            } else {
                                send.send(Dict.createFailed(getLockPath(completePath, lockPath)));
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("RMD")) {
                        if (userProps.getPermission().contains("d")) {
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
                                send.send(Dict.rmdSuccess());
                            } else if (file.isFile()) {
                                file.delete();
                                send.send(Dict.rmdSuccess());
                            } else {
                                send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("DELE")) {
                        if (userProps.getPermission().contains("d")) {
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
                                send.send(Dict.deleSuccess());
                            } else {
                                send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("RNFR")) {
                        if (userProps.getPermission().contains("m")) {
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
                                send.send(Dict.rnfrSuccess());
                            } else {
                                send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("RNTO")) {
                        if (userProps.getPermission().contains("m")) {
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
                                send.send(Dict.rntoSuccess());
                            } else {
                                send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                            }
                            RNFR = null;
                        } else {
                            send.send(Dict.permissionDenied());
                        }
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
                        portMode = new PORT(send, privateVariable, pauseListen, type, OnlineRules.getSpeedLimit(loginUser));
                        portMode.setTarget(ip, port);
                        send.send(Dict.portSuccess());
                        mode = "port";
                    } else if (cmd.equals("PASV")) {
                        if (passiveMode != null) {
                            passiveMode.stopSocket();
                        }
                        passiveMode = new PASV(send, privateVariable, pauseListen, type, OnlineRules.getSpeedLimit(loginUser));
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
                        send.send(Dict.pasvMode(IPADD, calcPort, randomSub));
                        passiveMode.start();
                        mode = "passive";
                    } else if (cmd.equals("RETR")) {
                        if (userProps.getPermission().contains("r")) {
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
                                    if (type.equals("I")) {
                                        send.send(Dict.openPasvBin(getLockPath(completePath, lockPath), file.length()));
                                    } else {
                                        send.send(Dict.openPasvAscii(getLockPath(completePath, lockPath), file.length()));
                                    }
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
                                    send.send(Dict.notFound(getLockPath(completePath, lockPath)));
                                }
                            } catch (NullPointerException NPE) {
                                send.send(Dict.pasvDataFailed());
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("STOR")) {
                        if (userProps.getPermission().contains("w")) {
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
                            if (type.equals("I")) {
                                send.send(Dict.openBin(getLockPath(completePath, lockPath)));
                            } else {
                                send.send(Dict.openAscii(getLockPath(completePath, lockPath)));
                            }
                            try {
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
                                send.send(Dict.pasvDataFailed());
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("SIZE")) {
                        if (userProps.getPermission().contains("r")) {
                            String completePath = arg1;
                            if (arg2 != null) {
                                for (int i = 2; i < split.length; i++) {
                                    completePath += " " + split[i];
                                }
                            }
                            completePath = getAbsolutePath(completePath);
                            File file = new File(completePath);
                            if (file.exists() && file.isFile()) {
                                send.send(Dict.fileSize(file.length()));
                            } else {
                                send.send(Dict.noSuchFile(completePath));
                            }
                        } else {
                            send.send(Dict.permissionDenied());
                        }
                    } else if (cmd.equals("OPTS")) {
                        if (arg1 != null) {
                            arg1 = arg1.toUpperCase();
                            if (arg1.equals("UTF8")) {
                                if (arg2 != null) {
                                    arg2 = arg2.toUpperCase();
                                    if (arg2.equals("ON")) {
                                        privateVariable.setEncode("UTF-8");
                                        privateVariable.setEncodeLock(true);
                                        send.send(Dict.utf8(true));
                                    } else if (arg2.equals("OFF")) {
                                        privateVariable.setEncode(Variable.defaultEncode);
                                        privateVariable.setEncodeLock(true);
                                        send.send(Dict.utf8(false));
                                    }
                                }
                            }
                        }
                    } else if (cmd.equals("REST")) {
                        if (arg1 != null) {
                            send.send(Dict.rest(arg1));
                            try {
                                privateVariable.setRest(Long.parseLong(arg1));
                            } catch (Exception E) {
                                E.printStackTrace();
                            }
                        }
                    } else if (cmd.equals("ABOR")) {
                        send.send(Dict.bye());
                        privateVariable.setInterrupted(true);
                    } else if (cmd.equals("GB")) {
                        privateVariable.setEncode("GB2312");
                        privateVariable.setEncodeLock(true);
                        send.send(Dict.gbEncodeOK(ipAddressBind.getIPADD()));
                    } else {
                        unknownCommand();
                    }
                    break;
            }
        }
    }

    public boolean unknownCommand() {
        send.send(Dict.unknownCommand());
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
        if (!getLockPath(currentPath, lockPath).equals("/")) {
            String[] dir = currentPath.split("/");
            currentPath = "";
            for (int i = 0; i < dir.length - 1; i++) {
                Logger.log(Types.SYS, Levels.DEBUG, "len: " + dir.length + " cur: " + i);
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
                path = lockPath;
            } else {
                path = lockPath + "/" + path;
            }
        } else {
            path = currentPath + "/" + path;
        }
        path = path.replaceAll("//", "/");
        Logger.log(Types.SYS, Levels.DEBUG, "Absolute path: " + path);
        return path;
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
        Logger.log(Types.SYS, Levels.DEBUG, count + " times while generating port: " + finalPort);
        map.put("randomPort", randomPort);
        map.put("randomSub", randomSub);
        map.put("calcPort", calcPort);
        map.put("finalPort", finalPort);
        return map;
    }
}
