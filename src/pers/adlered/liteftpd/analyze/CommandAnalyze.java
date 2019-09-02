package pers.adlered.liteftpd.analyze;

import java.io.*;
import java.util.Random;

import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.main.PauseListen;
import pers.adlered.liteftpd.main.Send;
import pers.adlered.liteftpd.mode.PASV;
import pers.adlered.liteftpd.tool.GoodXX;
import pers.adlered.liteftpd.tool.RandomNum;
import pers.adlered.liteftpd.user.Permission;

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
                     * NORMAL COMMANDS
                     */
                    else if (cmd.equals("PWD")) {
                        //if (file.isDirectory()) {
                            send.send(Dict.currentDir + "\"" + currentPath + "\" is current directory." + "\r\n");
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
                        send.send("250 Directory changed to " + currentPath + "\r\n");
                    }
                    else if (cmd.equals("CWD")) {
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
                        } else {
                            completePath = completePath.replaceAll("/$", "");
                        }
                        if (completePath.equals("..")) {
                            upperDirectory();
                            send.send(Dict.changeDir + currentPath + Dict.newLine);
                        } else {
                            if (completePath.indexOf("/") != -1 && completePath.indexOf("./") == -1) {
                                //Absolute path.
                                //currentPath = completePath;
                            } else {
                                //Relative path.
                                completePath = currentPath + "/" + completePath;
                            }
                            File file = new File(completePath);
                            if(file.exists()) {
                                currentPath = completePath;
                                send.send(Dict.changeDir + currentPath + Dict.newLine);
                            } else {
                                send.send(Dict.noSuchFileOrDir + completePath + Dict.noSuchFIleOrDir2);
                            }
                        }
                    }
                    else if (cmd.equals("SYST")) {
                        send.send(Dict.type);
                    }
                    /**
                     * TRANSMISSION COMMANDS
                     */
                    else if (cmd.equals("PASV")) {
                        Random random = new Random();
                        /*int randomPort = random.nextInt(55296) + 10240;
                        int randomSub = random.nextInt(5000) + 5000;
                        int calcPort = (randomPort - randomSub) / 256;
                        int finalPort = calcPort * 256 + randomSub;*/
                        int randomPort = RandomNum.sumIntger(1024, 40960, false);
                        int randomSub = RandomNum.sumIntger(0, 64, false);
                        int calcPort = (randomPort - randomSub) / 256;
                        int finalPort = calcPort * 256 + randomSub;
                        passiveMode = new PASV(finalPort, send, privateVariable, pauseListen);
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
                        if (completePath.indexOf("/") == -1 || completePath.indexOf("./") != -1) {
                            //Relative path.
                            completePath = currentPath + "/" + completePath;
                        }
                        try {
                            File file = new File(completePath);
                            if (file.exists()) {
                                send.send(Dict.openPassiveBINARY + file.getPath() + " (" + file.length() + " Bytes)\r\n");
                                passiveMode.hello(file);
                            } else {
                                send.send(Dict.noSuchFileOrDir + file.getPath() + Dict.noSuchFIleOrDir2);
                            }
                        } catch (NullPointerException NPE) {
                            send.send(Dict.passiveDataFailed);
                        }
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

}
