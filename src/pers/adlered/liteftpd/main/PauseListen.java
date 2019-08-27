package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.variable.ChangeVar;
import pers.adlered.liteftpd.variable.Variable;

import java.io.*;
import java.net.Socket;

public class PauseListen extends Thread {
    private PrivateVariable privateVariable = null;
    private Socket socket = null;
    private Receive receive = null;
    private BufferedOutputStream bufferedOutputStream = null;
    private OutputStream outputStream = null;
    private BufferedInputStream bufferedInputStream = null;
    private InputStream inputStream = null;
    private String IPADD = null;

    private int timeout = 0;

    private boolean running = true;

    public PauseListen(PrivateVariable privateVariable, Socket socket, Receive receive, BufferedOutputStream bufferedOutputStream, OutputStream outputStream, BufferedInputStream bufferedInputStream, InputStream inputStream, String IPADD) {
        this.privateVariable = privateVariable;
        this.socket = socket;
        this.receive = receive;
        this.bufferedOutputStream = bufferedOutputStream;
        this.outputStream = outputStream;
        this.bufferedInputStream = bufferedInputStream;
        this.inputStream = inputStream;
        this.IPADD = IPADD;
    }

    @Override
    public void run() {
        String reason = "User quit manually";
        while (!privateVariable.interrupted) {
            System.out.println(IPADD + " timeout: " + timeout);
            if (timeout >= Variable.timeout) {
                reason = "Time is out";
                break;
            }
            try {
                ++timeout;
                Thread.sleep(1000);
            } catch (InterruptedException IE) {
                break;
            }
        }
        System.out.println("\r\nShutting down " + IPADD + ", because: " + reason);
        running = false;
        ChangeVar.reduceOnlineCount();
        try {
            bufferedInputStream.close();
            inputStream.close();
            bufferedOutputStream.close();
            outputStream.close();
            privateVariable = null;
        } catch (Exception E) {
            System.out.println("Shutting " + IPADD + " with errors.");
        }
    }

    public void resetTimeout() {
        timeout = 0;
    }

    public boolean isRunning() {
        return running;
    }
}