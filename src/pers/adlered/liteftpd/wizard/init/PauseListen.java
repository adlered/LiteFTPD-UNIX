package pers.adlered.liteftpd.wizard.init;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.user.status.bind.IPAddressBind;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.enums.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.enums.Types;
import pers.adlered.liteftpd.tool.Status;
import pers.adlered.liteftpd.user.status.bind.IpLimitBind;
import pers.adlered.liteftpd.variable.OnlineUserController;
import pers.adlered.liteftpd.variable.Variable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Listening thread shutdown signal, and recycle the threads of this connection.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class PauseListen extends Thread {
    private PrivateVariable privateVariable = null;
    private Socket socket = null;
    private BufferedOutputStream bufferedOutputStream = null;
    private OutputStream outputStream = null;
    private BufferedInputStream bufferedInputStream = null;
    private InputStream inputStream = null;
    private IPAddressBind ipAddressBind = null;

    private Send send = null;
    private CommandAnalyze commandAnalyze = null;
    private Receive receive = null;
    private IpLimitBind ipLimitBind = null;

    private int timeout = 0;

    private boolean running = true;

    public PauseListen(PrivateVariable privateVariable, Socket socket,
                       BufferedOutputStream bufferedOutputStream,
                       OutputStream outputStream, BufferedInputStream bufferedInputStream,
                       InputStream inputStream, IPAddressBind ipAddressBind,
                       CommandAnalyze commandAnalyze, Receive receive, IpLimitBind ipLimitBind) {
        this.privateVariable = privateVariable;
        this.socket = socket;
        this.bufferedOutputStream = bufferedOutputStream;
        this.outputStream = outputStream;
        this.bufferedInputStream = bufferedInputStream;
        this.inputStream = inputStream;
        this.ipAddressBind = ipAddressBind;
        this.send = send;
        this.commandAnalyze = commandAnalyze;
        this.receive = receive;
        this.ipLimitBind = ipLimitBind;
    }

    public void setSend(Send send) {
        this.send = send;
    }

    @Override
    public void run() {
        String reason = "User quit manually";
        int skipCount = 0;
        while (!privateVariable.interrupted) {
            if (privateVariable.isTimeoutLock()) {
                ++skipCount;
                resetTimeout();
            }
            if (skipCount % 10 == 0 && skipCount != 0) {
                Logger.log(Types.SYS, Levels.DEBUG, ipAddressBind.getIPADD() + " skipped timeout: " + skipCount);
                if (skipCount > Variable.maxTimeout) {
                    reason = "Time is out while in transmission.";
                    break;
                }
            }
            // Display log every 10 seconds.
            if (timeout % 10 == 0 && timeout != 0) {
                Logger.log(Types.SYS, Levels.DEBUG, ipAddressBind.getIPADD() + " timeout: " + timeout + "=>" + Variable.timeout);
            }
            if (timeout >= Variable.timeout) {
                reason = "Time is out";
                break;
            }
            try {
                ++timeout;
                Thread.sleep(1000);
            } catch (InterruptedException IE) {
                break;
            }
        }
        if (privateVariable.reason != null) {
            reason = privateVariable.reason;
            privateVariable.reason = null;
        }
        send.send("LiteFTPD > :( Sorry, the connection is closed from server! Reason: " + reason + "." + Dict.newLine + "");
        Logger.log(Types.SYS, Levels.INFO, "Shutting down " + ipAddressBind.getIPADD() + ", reason: " + reason);
        // Shutdown this hole connection.
        running = false;
        OnlineUserController.reduceOnline(socket.getInetAddress().getHostAddress(), privateVariable.getUsername());
        OnlineUserController.printOnline();
        try {
            // BufferedStream
            bufferedInputStream.close();
            bufferedOutputStream.close();
            // Stream
            inputStream.close();
            outputStream.close();
            // Socket
            socket.close();
        } catch (Exception E) {
            E.getCause();
            Logger.log(Types.SYS, Levels.WARN, "Shutting " + ipAddressBind.getIPADD() + " with errors.");
        } finally {
            // Variables
            bufferedInputStream = null;
            bufferedOutputStream = null;
            inputStream = null;
            outputStream = null;
            ipAddressBind = null;
            socket = null;
            privateVariable = null;
            send = null;
            commandAnalyze = null;
            receive = null;
            ipLimitBind = null;
            Logger.log(Types.SYS, Levels.DEBUG, "Called Garbage Collection.");
            System.gc();
            Logger.log(Types.SYS, Levels.INFO, "Memory used: " + Status.memoryUsed());
        }
    }

    public void resetTimeout() {
        timeout = 0;
    }

    public boolean isRunning() {
        return running;
    }
}