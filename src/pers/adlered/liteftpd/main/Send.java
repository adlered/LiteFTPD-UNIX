package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.dict.Dict;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Send {
    private BufferedOutputStream bufferedOutputStream = null;
    private String IPADD = null;

    private PauseListen pauseListen = null;

    public Send(BufferedOutputStream bufferedOutputStream, String IPADD, PauseListen pauseListen) {
        this.bufferedOutputStream = bufferedOutputStream;
        this.IPADD = IPADD;
        this.pauseListen = pauseListen;
        try {
            //WELCOME MESSAGE
            bufferedOutputStream.write(Dict.connectionStarted.getBytes());
            bufferedOutputStream.flush();
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }

    public boolean send(String message) {
        try {
            System.out.println(message);
            pauseListen.resetTimeout();
            //WELCOME MESSAGE
            bufferedOutputStream.write(message.getBytes());
            bufferedOutputStream.flush();
            return true;
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
            return false;
        }
    }
}
