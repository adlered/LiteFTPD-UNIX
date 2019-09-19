package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Code;
import pers.adlered.liteftpd.dict.Dict;
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
        send(Code.SERVICEREADY + "-LiteFTPD\r\n>>> Please log in, my honored guest.\r\n>>> Your IP address: " + ipAddressBind.getIPADD() + "\r\n220" + Dict.connectionStarted);
    }

    public boolean send(String message) {
        //System.out.println("Encode is: " + privateVariable.getEncode());
        try {
            System.out.println(ipAddressBind.getIPADD() + " <== [ " + privateVariable.encode + " ] " + ipAddressBind.getSRVIPADD() + ": " + message.replaceAll("\r|\n", ""));
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
