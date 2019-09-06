package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Code;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.tool.GoodXX;
import pers.adlered.liteftpd.variable.Variable;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Send {
    private OutputStream outputStream = null;

    private PauseListen pauseListen = null;
    private PrivateVariable privateVariable = null;
    private IPAddressBind ipAddressBind = null;

    public Send(OutputStream outputStream, PauseListen pauseListen, PrivateVariable privateVariable, IPAddressBind ipAddressBind) {
        this.outputStream = outputStream;
        this.pauseListen = pauseListen;
        this.privateVariable = privateVariable;
        this.ipAddressBind = ipAddressBind;
        send(Code.SERVICEREADY + " LiteFTPD > Have a nice day, " + ipAddressBind.getIPADD() + Dict.connectionStarted);
    }

    public boolean send(String message) {
        System.out.println("Encode is: " + privateVariable.getEncode());
        try {
            System.out.println(ipAddressBind.getIPADD() + " <== " + ipAddressBind.getSRVIPADD() + ": " + message.replaceAll("\r|\n", ""));
            pauseListen.resetTimeout();
            //WELCOME MESSAGE
            outputStream.write(message.getBytes(privateVariable.encode));
            outputStream.flush();
            return true;
        } catch (IOException IOE) {
            //TODOx
            IOE.printStackTrace();
            return false;
        }
    }
}
