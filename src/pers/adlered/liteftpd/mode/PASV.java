package pers.adlered.liteftpd.mode;

import com.sun.security.ntlm.Server;
import pers.adlered.liteftpd.main.Send;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PASV extends Thread {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private Send send = null;

    private String listening = null;

    public PASV(int port, Send send) {
        System.out.println("listening " + port);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            this.serverSocket = serverSocket;
            this.send = send;
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("WAITING...");
            Socket socket = serverSocket.accept();
            this.socket = socket;
            System.out.println(socket.getInetAddress() + " connected!");
            System.out.print("> Waiting");
            while (listening == null) { System.out.print("."); try { Thread.sleep(500); } catch (InterruptedException IE) {}}
            System.out.print("\r\nService has response.");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedOutputStream.write(listening.getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            socket.close();
            serverSocket.close();
            send.send("226 Transfer complete. 148 bytes transferred. 0.25 KB/sec.\r\n");
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }

    public void hello(String message) {
        listening = message;
    }
}
