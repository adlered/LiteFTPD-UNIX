package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.enums.Types;
import pers.adlered.liteftpd.pool.handler.HandlerPool;
import pers.adlered.liteftpd.tool.ConsoleTable;
import pers.adlered.liteftpd.tool.LocalAddress;
import pers.adlered.liteftpd.tool.Status;
import pers.adlered.liteftpd.user.User;
import pers.adlered.liteftpd.user.status.Online;
import pers.adlered.liteftpd.user.status.bind.IpLimitBind;
import pers.adlered.liteftpd.user.verify.OnlineRules;
import pers.adlered.liteftpd.variable.Variable;
import pers.adlered.liteftpd.wizard.config.Prop;
import pers.adlered.liteftpd.wizard.init.SocketHandler;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Main method of LiteFTPD, listening connections and create a new thread into thread pool.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Main {
    public static void main(String[] args) {
        Main.init(args);
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread() {
            @Override
            public void run() {
                Logger.log(Types.SYS, Levels.INFO, "LiteFTPD stopped.");
            }
        });
        ServerSocket serverSocket = null;
        try {
            // 先读取配置文件
            Prop.getInstance();
            // 再初始化用户信息
            User.initUsers();
            Logger.log(Types.SYS, Levels.INFO, "LiteFTPD by AdlerED <- GitHub");
            // Listen socket connections, handle with SocketHandler.
            serverSocket = new ServerSocket(Variable.port);
            Logger.log(Types.SYS, Levels.INFO, "Listening " + serverSocket.getLocalSocketAddress());
            Logger.log(Types.SYS, Levels.INFO, "You can connect to the FTP Server via following IP address:");
            ConsoleTable consoleTable = new ConsoleTable(LocalAddress.getLocalIPList().size(), true);
            consoleTable.appendRow();
            consoleTable.appendColum("Listening IP:Port")
                    .appendColum("github.com/AdlerED");
            consoleTable.appendRow();
            if (Variable.debugLevel >= 1) {
                for (String i : LocalAddress.getLocalIPList()) {
                    consoleTable.appendColum(i + ":" + serverSocket.getLocalPort());
                }
            }
            System.out.println(consoleTable.toString());
        } catch (IOException IOE) {
            // TODO
            IOE.printStackTrace();
        }
        while (true) {
            try {
                Logger.log(Types.SYS, Levels.INFO, "Memory used: " + Status.memoryUsed());
                Socket socket = serverSocket.accept();
                // Online limit checking
                String hostAdd = socket.getInetAddress().getHostAddress();
                IpLimitBind ipLimitBind = OnlineRules.checkIpAddress(hostAdd);
                if (((ipLimitBind.getIp() == null) || Variable.online >= Variable.maxUserLimit) && Variable.maxUserLimit != 0) {
                    for (int i = 0; i < Online.ipRuleOnline.size(); i++) {
                        if (Online.ipRuleOnline.get(i).getIp().equals(hostAdd)) {
                            Online.ipRuleOnline.remove(i);
                            break;
                        }
                    }
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                    bufferedOutputStream.write(Dict.outOfOnlineLimit().getBytes());
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    socket.close();
                } else {
                    HandlerPool.handlerPool.execute(new SocketHandler(socket, ipLimitBind));
                }
            } catch (IOException IOE) {
                // TODO
                IOE.printStackTrace();
            }
        }
    }

    public static void init(String[] args) {
        Dict.init("en_us");
        for (int i = 0; i < args.length; i += 2) {
            String variable = args[i];
            String value = args[i + 1];
            if (variable.equals("-l")) {
                value = value.replaceAll("-", "_");
                if (value.equals("en_us")) {
                    Logger.log(Types.SYS, Levels.INFO, "Language option detected. Init language as \"English\".");
                    Dict.init(value);
                } else if (value.equals("zh_cn")) {
                    Logger.log(Types.SYS, Levels.INFO, "Language option detected. Init language as \"简体中文\".");
                    Dict.init(value);
                } else {
                    Logger.log(Types.SYS, Levels.WARN, "Cannot support customize language \"" + value + "\". Using default \"English\".");
                    Logger.log(Types.SYS, Levels.WARN, "Supported language: zh_cn en_us");
                }
            }
        }
    }
}
