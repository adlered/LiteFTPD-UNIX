package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Code;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.tool.GoodXX;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Send {
    private BufferedOutputStream bufferedOutputStream = null;

    private PauseListen pauseListen = null;
    private PrivateVariable privateVariable = null;
    private IPAddressBind ipAddressBind = null;

    public Send(BufferedOutputStream bufferedOutputStream, PauseListen pauseListen, PrivateVariable privateVariable, IPAddressBind ipAddressBind) {
        this.bufferedOutputStream = bufferedOutputStream;
        this.pauseListen = pauseListen;
        this.privateVariable = privateVariable;
        this.ipAddressBind = ipAddressBind;
        send(Code.SERVICEREADY + " Hello: " + ipAddressBind.getIPADD() + "!" + Dict.connectionStarted);
    }

    public boolean send(String message) {
        System.out.println("Encode is: " + privateVariable.getEncode());
        try {
            System.out.println(ipAddressBind.getSRVIPADD() + " => " + ipAddressBind.getIPADD() + ": " + message.replaceAll("\r|\n", ""));
            pauseListen.resetTimeout();
            //WELCOME MESSAGE
            bufferedOutputStream.write(message.getBytes(privateVariable.encode));
            bufferedOutputStream.flush();
            return true;
        } catch (IOException IOE) {
            //TODOx
            IOE.printStackTrace();
            return false;
        }
    }
}
