package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.variable.ChangeVar;

import java.io.*;
import java.net.Socket;
import java.net.SocketImpl;

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

    Send send = null;
    CommandAnalyze commandAnalyze = null;
    Receive receive = null;

    PrivateVariable privateVariable = null;

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
            privateVariable = new PrivateVariable();
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println(IPADD + " has been mounted into " + Thread.currentThread());
        //Process while user quit forced or manually.
        PauseListen pauseListen = new PauseListen(privateVariable, socket, receive, bufferedOutputStream, outputStream, bufferedInputStream, inputStream, IPADD);
        pauseListen.start();
        //Start model
        send = new Send(bufferedOutputStream, IPADD, pauseListen);
        commandAnalyze = new CommandAnalyze(send, SRVIPADD, privateVariable, pauseListen);
        receive = new Receive(inputStream, IPADD, commandAnalyze, pauseListen);
        receive.start();
    }
}
