package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Code;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Send method can be called by other threads in the same thread pool, and send a message to client.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
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
        Logger.log(Types.SEND, Levels.DEBUG, "Encode is: " + privateVariable.getEncode());
        try {
            Logger.log(Types.SEND, Levels.INFO, ipAddressBind.getIPADD() + " <== [ " + privateVariable.encode + " ] " + ipAddressBind.getSRVIPADD() + ": " + message.replaceAll("\r|\n", ""));
            pauseListen.resetTimeout();
            // WELCOME MESSAGE
            outputStream.write(message.getBytes(privateVariable.encode));
            outputStream.flush();
            return true;
        } catch (IOException IOE) {
            // TODOx
            IOE.printStackTrace();
            return false;
        }
    }
}
