package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.dict.Dict;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Send {
    private BufferedOutputStream bufferedOutputStream;
    private String IPADD;

    public Send(BufferedOutputStream bufferedOutputStream, String IPADD) {
        this.bufferedOutputStream = bufferedOutputStream;
        this.IPADD = IPADD;
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
