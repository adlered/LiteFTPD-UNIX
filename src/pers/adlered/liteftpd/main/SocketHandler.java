package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;

import java.io.*;
import java.net.Socket;

public class SocketHandler extends Thread {
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private BufferedInputStream bufferedInputStream = null;
    private BufferedOutputStream bufferedOutputStream = null;
    private Socket socket = null;

    private String IPADD = null;
    private String SRVIPADD = null;

    //Sign 2 to tell main thread kill itself
    private boolean interrupted = false;

    public SocketHandler(Socket socket) {
        try {
            IPADD = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
            SRVIPADD = socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort();
            //Import data streams.
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            //Input stream use buffer, but output is not because of trans situations.
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            this.socket = socket;
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println(IPADD + " has been mounted into " + Thread.currentThread());
        //Start model
        Send send = new Send(bufferedOutputStream, IPADD);
        CommandAnalyze commandAnalyze = new CommandAnalyze(send, SRVIPADD);
        Thread receive = new Receive(inputStream, IPADD, commandAnalyze);
        receive.start();
        while (!commandAnalyze.interrupted) {
            System.out.print(".");
            try {
                socket.sendUrgentData(0xFF);
                Thread.sleep(500);
            } catch (InterruptedException IE) {
            } catch (IOException IOE) {
                break;
            }
        }
        System.out.println("Shutting down " + IPADD);
        try {
            receive.stop();
            bufferedOutputStream.flush();
            outputStream.flush();
            bufferedInputStream.close();
            inputStream.close();
            bufferedOutputStream.close();
            outputStream.close();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }
}
