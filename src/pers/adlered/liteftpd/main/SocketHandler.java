package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;

import java.io.*;
import java.net.Socket;

public class SocketHandler extends Thread {
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private String IPADD;

    public SocketHandler(Socket socket) {
        try {
            IPADD = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
            //Import data streams.
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            //Input stream use buffer, but output is not because of trans situations.
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
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
        CommandAnalyze commandAnalyze = new CommandAnalyze(send);
        Thread receive = new Receive(inputStream, IPADD, commandAnalyze);
        receive.start();
    }
}
