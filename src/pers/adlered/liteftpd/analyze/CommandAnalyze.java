package pers.adlered.liteftpd.analyze;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.Random;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.main.Send;
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

    private Send send;

    File file;

    public CommandAnalyze(Send send) {
        this.send = send;
        file = new File(Permission.defaultDir);
    }

    public void analyze(String command) {
        String cmd = null;
        String arg1 = null;
        String arg2 = null;
        try {
            String[] split = command.split(" ");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].replaceAll("(\r|\n)", "");
            }
            cmd = split[0];
            switch (split.length) {
                case 2:
                    arg1 = split[1];
                    break;
                case 3:
                    arg2 = split[2];
                    break;
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
                        System.out.println(Thread.currentThread() + " login: " + arg1);
                        loginUser = arg1;
                        send.send(Dict.passwordRequired + loginUser + "." + "\r\n");
                        step = 2;
                    } else {
                        unknownCommand();
                    }
                    break;
                case 2:
                    if (cmd.equals("PASS")) {
                        System.out.println("User " + loginUser + " login: " + arg1);
                        loginPass = arg1;
                        send.send(Dict.loggedIn + loginUser + " logged." + "\r\n");
                        step = 3;
                    } else {
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
                        if (file.isDirectory()) {
                            send.send(Dict.currentDir + "\"" + file.getAbsolutePath() + "\" is current directory." + "\r\n");
                        } else {
                            send.send(Dict.isFile + file.getName());
                        }
                    }
                    else if (cmd.equals("TYPE")) {
                        arg1 = arg1.toUpperCase();
                        switch (arg1) {
                            case "I":
                                send.send(Dict.setType + "I." + "\r\n");
                                break;
                            case "A":
                                send.send(Dict.setType + "A." + "\r\n");
                                break;
                        }
                    }
                    /**
                     * TRANSMISSION COMMANDS
                     */
                    else if (cmd.equals("PASV")) {
                        Random random = new Random();
                        int randomPort = random.nextInt(55296) + 10240;
                        send.send(Dict.passiveMode + "(" + 1 + "," + 1 + "," + 1 + "," + 1 + "," + 1 + "," + 1 + ")." + "\r\n");
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
}
