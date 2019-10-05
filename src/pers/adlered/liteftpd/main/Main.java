package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;
import pers.adlered.liteftpd.pool.Pool;
import pers.adlered.liteftpd.tool.ConsoleTable;
import pers.adlered.liteftpd.tool.LocalAddress;
import pers.adlered.liteftpd.tool.Status;
import pers.adlered.liteftpd.user.User;
import pers.adlered.liteftpd.variable.ChangeVar;
import pers.adlered.liteftpd.variable.Variable;
import pers.adlered.liteftpd.wizard.config.Prop;

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
                if (Variable.online >= Variable.maxUserLimit && Variable.maxUserLimit != 0) {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                    bufferedOutputStream.write(Dict.outOfOnlineLimit.getBytes());
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    socket.close();
                } else {
                    ChangeVar.plusOnlineCount();
                    Pool.handlerPool.execute(new SocketHandler(socket));
                }
            } catch (IOException IOE) {
                // TODO
                IOE.printStackTrace();
            }
        }
    }
}
