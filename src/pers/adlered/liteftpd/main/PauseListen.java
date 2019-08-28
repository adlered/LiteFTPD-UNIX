package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.tool.Status;
import pers.adlered.liteftpd.variable.ChangeVar;
import pers.adlered.liteftpd.variable.Variable;

import java.io.*;
import java.net.Socket;

public class PauseListen extends Thread {
    private PrivateVariable privateVariable = null;
    private Socket socket = null;
    private BufferedOutputStream bufferedOutputStream = null;
    private OutputStream outputStream = null;
    private BufferedInputStream bufferedInputStream = null;
    private InputStream inputStream = null;
    private IPAddressBind ipAddressBind = null;

    private Send send = null;
    private CommandAnalyze commandAnalyze = null;
    private Receive receive = null;

    private int timeout = 0;

    private boolean running = true;

    public PauseListen(PrivateVariable privateVariable, Socket socket,
                       BufferedOutputStream bufferedOutputStream,
                       OutputStream outputStream, BufferedInputStream bufferedInputStream,
                       InputStream inputStream, IPAddressBind ipAddressBind,
                       Send send, CommandAnalyze commandAnalyze, Receive receive) {
        this.privateVariable = privateVariable;
        this.socket = socket;
        this.bufferedOutputStream = bufferedOutputStream;
        this.outputStream = outputStream;
        this.bufferedInputStream = bufferedInputStream;
        this.inputStream = inputStream;
        this.ipAddressBind = ipAddressBind;
        this.send = send;
        this.commandAnalyze = commandAnalyze;
        this.receive = receive;
    }

    @Override
    public void run() {
        String reason = "User quit manually";
        while (!privateVariable.interrupted) {
            if (privateVariable.isTimeoutLock()) {
                resetTimeout();
            }
            //Display log every 10 seconds.
            if (timeout % 10 == 0 && timeout != 0) {
                System.out.println(ipAddressBind.getIPADD() + " timeout: " + timeout + "=>" + Variable.timeout);
            }
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
        System.out.println("Shutting down " + ipAddressBind.getIPADD() + ", because: " + reason);
        //Shutdown this hole connection.
        running = false;
        ChangeVar.reduceOnlineCount();
        try {
            //BufferedStream
            bufferedInputStream.close();
            bufferedOutputStream.close();
            //Stream
            inputStream.close();
            outputStream.close();
            //Socket
            socket.close();
        } catch (Exception E) {
            E.getCause();
            System.out.println("Shutting " + ipAddressBind.getIPADD() + " with errors.");
        } finally {
            //Variables
            bufferedInputStream = null;
            bufferedOutputStream = null;
            inputStream = null;
            outputStream = null;
            ipAddressBind = null;
            socket = null;
            privateVariable = null;
            send = null;
            commandAnalyze = null;
            receive = null;
            System.out.println("Called Garbage Collection.");
            System.gc();
            System.out.println("Memory used: " + Status.memoryUsed());
        }
    }

    public void resetTimeout() {
        timeout = 0;
    }

    public boolean isRunning() {
        return running;
    }
}