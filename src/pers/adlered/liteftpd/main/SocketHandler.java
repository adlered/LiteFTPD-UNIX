package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;

import java.io.*;
import java.net.Socket;

public class SocketHandler extends Thread {
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private BufferedInputStream bufferedInputStream = null;
    private BufferedOutputStream bufferedOutputStream = null;
    private Socket socket = null;
    private IPAddressBind ipAddressBind = null;

    private String IPADD = null;
    private String SRVIPADD = null;

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
        Logger.log(Types.SYS, Levels.INFO, IPADD + " has been mounted into " + Thread.currentThread());
        ipAddressBind = new IPAddressBind(IPADD, SRVIPADD);
        //Process while user quit forced or manually.
        PauseListen pauseListen = new PauseListen(privateVariable, socket,
                bufferedOutputStream, outputStream, bufferedInputStream,
                inputStream, ipAddressBind,
                send, commandAnalyze, receive
        );
        //Start model
        send = new Send(outputStream, pauseListen, privateVariable, ipAddressBind);
        commandAnalyze = new CommandAnalyze(send, SRVIPADD, privateVariable, pauseListen, ipAddressBind);
        receive = new Receive(inputStream, commandAnalyze, pauseListen, privateVariable, ipAddressBind);
        receive.start();
        pauseListen.start();
    }
}
