package pers.adlered.liteftpd.analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.main.Send;
import pers.adlered.liteftpd.mode.PASV;
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

    private boolean inPassiveMode = false;

    public CommandAnalyze(Send send, String SRVIPADD) {
        this.send = send;
        this.SRVIPADD = SRVIPADD;
        file = new File(Permission.defaultDir);
    }

    public void analyze(String command) {
        System.out.println(command);
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
                    else if (cmd.equals("BYE") || cmd.equals("QUIT")) {
                        send.send(Dict.bye);
                        //TODO Close server socket connection
                    }
                    else if (cmd.equals("LIST")) {
                        send.send("150 Opening ASCII mode data connection for /bin/ls.\r\n");
                        try {
                            Process process = Runtime.getRuntime().exec(new String[]{"ls", "-l"});
                            process.waitFor();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            StringBuilder result = new StringBuilder();
                            while ((line = bufferedReader.readLine()) != null) {
                                result.append(line).append('\n');
                            }
                            passiveMode.hello(result.toString());
                        } catch (IOException IOE) {
                            //TODO
                            IOE.printStackTrace();
                        } catch (InterruptedException IE) {
                            //TODO
                            IE.printStackTrace();
                        }
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
                        passiveMode = new PASV(finalPort, send);
                        String[] IPADD = (SRVIPADD.split(":")[0]).split("\\.");
                        send.send(Dict.passiveMode + "(" + IPADD[0] + "," + IPADD[1] + "," + IPADD[2] + "," + IPADD[3] + "," + calcPort + "," + randomSub + ")" + "\r\n");
                        if (inPassiveMode) {
                            passiveMode.stop();
                        }
                        passiveMode.start();
                        inPassiveMode = true;
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
