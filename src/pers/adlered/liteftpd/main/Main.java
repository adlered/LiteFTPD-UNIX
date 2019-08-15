package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.pool.Pool;
import pers.adlered.liteftpd.variable.ChangeVar;
import pers.adlered.liteftpd.variable.Variable;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            //Listen socket connections, handle with SocketHandler.
            serverSocket = new ServerSocket(Variable.port);
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
        while (true) {
            try {
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
                    SocketHandler socketHandler = new SocketHandler(socket);
                    Pool.handlerPool.execute(socketHandler);
                }
            } catch (IOException IOE) {
                //TODO
                IOE.printStackTrace();
            }
        }
    }
}
