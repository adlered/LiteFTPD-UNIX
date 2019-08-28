package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.pool.Pool;
import pers.adlered.liteftpd.tool.Status;
import pers.adlered.liteftpd.variable.ChangeVar;
import pers.adlered.liteftpd.variable.Variable;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            //Listen socket connections, handle with SocketHandler.
            serverSocket = new ServerSocket(Variable.port);
            System.out.println("Listening " + serverSocket.getLocalSocketAddress());
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("Memory used: " + Status.memoryUsed());
                Socket socket = serverSocket.accept();
                //Online limit checking
                if (Variable.online >= Variable.maxUserLimit) {
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
                //TODO
                IOE.printStackTrace();
            }
        }
    }
}
