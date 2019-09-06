package pers.adlered.liteftpd.analyze;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.main.PauseListen;
import pers.adlered.liteftpd.main.Send;
import pers.adlered.liteftpd.mode.PASV;
import pers.adlered.liteftpd.tool.GoodXX;
import pers.adlered.liteftpd.tool.RandomNum;
import pers.adlered.liteftpd.user.Permission;
import pers.adlered.liteftpd.variable.Variable;

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

    File file = null;
    PASV passiveMode = null;

    private String currentPath = "";
    private String lockPath = "";
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
                        System.out.println(Thread.currentThread() + " login: " + arg1);
                        loginUser = arg1;
                        send.send(Dict.passwordRequired + loginUser + "." + "\r\n");
                        step = 2;
                    }
                    else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye);
                        privateVariable.setInterrupted(true);
                    }
                    else {
                        unknownCommand();
                    }
                    break;
                case 2:
                    if (cmd.equals("PASS")) {
                        System.out.println("User " + loginUser + " login: " + arg1);
                        loginPass = arg1;
                        send.send(Dict.loggedIn + ":) Good " + GoodXX.getTimeAsWord() + ", " + loginUser + "!" + Dict.remind);
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
                            System.out.println(result.toString());
                            try {
                                passiveMode.hello(result.toString());
                            } catch (NullPointerException NPE) {
                                send.send(Dict.passiveDataFailed);
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
                    /**
                     * TRANSMISSION COMMANDS
                     */
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
                            System.out.println("Port listening to: " + finalPort);
                        } while (!passiveMode.listen(finalPort));
                        String[] IPADD = (SRVIPADD.split(":")[0]).split("\\.");
                        send.send(Dict.passiveMode + "(" + IPADD[0] + "," + IPADD[1] + "," + IPADD[2] + "," + IPADD[3] + "," + calcPort + "," + randomSub + ")" + "\r\n");
                        passiveMode.start();
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
                        System.out.println("Complete path: " + completePath);
                        completePath = getAbsolutePath(completePath);
                        try {
                            File file = new File(completePath);
                            if (file.exists()) {
                                send.send(Dict.openPassiveBINARY + getLockPath(completePath, Permission.defaultDir) + " (" + file.length() + " Bytes)\r\n");
                                passiveMode.hello(file);
                            } else {
                                send.send(Dict.noSuchFileOrDir + getLockPath(completePath, Permission.defaultDir) + Dict.noSuchFIleOrDir2);
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

    public String getLockPath(String absolutePath, String lockPath) {
        String resolve = absolutePath.replaceAll("^(" + lockPath + ")", "");
        if (resolve.isEmpty()) resolve = "/";
        return resolve;
    }

    public void upperDirectory() {
        //Depart && Re-part path
        if (!currentPath.equals("/")) {
            String[] dir = currentPath.split("/");
            currentPath = "";
            for (int i = 0; i < dir.length - 1; i++) {
                System.out.println("len: " + dir.length + " cur: " + i);
                if (i == dir.length - 2) {
                    currentPath += dir[i];
                } else {
                    currentPath += dir[i] + "/";
                }
            }
            if (currentPath.isEmpty()) currentPath = "/";
        }
        System.out.println(currentPath);
    }

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
        System.out.println("Absolute path: " + path);
        return path;
    }

    public static boolean isPortUsing(String host,int port) {
        boolean flag = false;
        try {
            Socket socket = new Socket(host, port);
            flag = true;
            socket.close();
        } catch (IOException IOE) {
            System.out.println("Port " + port + " already in use, re-generating...");
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
        System.out.println(count + " times while generating port: " + finalPort);
        map.put("randomPort", randomPort);
        map.put("randomSub", randomSub);
        map.put("calcPort", calcPort);
        map.put("finalPort", finalPort);
        return map;
    }
}
