package pers.adlered.liteftpd.mode;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PASV extends Thread {
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    public PASV(int port) {
        System.out.println("listening " + port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            this.serverSocket = serverSocket;
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Socket socket = serverSocket.accept();
            this.socket = socket;
            System.out.println(socket.getInetAddress() + " connected!");
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }
}
