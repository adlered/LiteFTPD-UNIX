package pers.adlered.liteftpd.mode;

import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;
import pers.adlered.liteftpd.main.PauseListen;
import pers.adlered.liteftpd.main.Send;
import pers.adlered.liteftpd.variable.Variable;

import java.io.*;
import java.net.*;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Passive mode.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class PORT extends Thread {
    private Socket socket = null;
    private Send send = null;
    private PrivateVariable privateVariable = null;
    private PauseListen pauseListen = null;

    private String listening = null;
    private File file = null;
    private String path = null;

    private String ip = null;
    private int port = -1;

    public PORT(Send send, PrivateVariable privateVariable, PauseListen pauseListen) {
        this.send = send;
        this.privateVariable = privateVariable;
        this.pauseListen = pauseListen;
    }

    public void setTarget(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private boolean connect() {
        boolean result = true;
        try {
            Logger.log(Types.SYS, Levels.DEBUG,"Connecting to " + ip + ":" + port + "...");
            Socket socket = new Socket(ip, port);
            this.socket = socket;
        } catch (UnknownHostException UHE) {
            result = false;
        } catch (IllegalArgumentException IAE) {
            result = false;
        } catch (IOException IOE) {
            result = false;
        }
        return result;
    }

    @Override
    public void run() {
        if (connect()) {
            try {
                Logger.log(Types.SYS, Levels.DEBUG,"Connected. Translating with " + socket.getLocalSocketAddress() + " to " + socket.getRemoteSocketAddress() + "...");
                privateVariable.setTimeoutLock(true);
                if (pauseListen.isRunning()) {
                    Logger.log(Types.SYS, Levels.DEBUG,"Port mode is in transmission.");
                    long startTime = System.nanoTime();
                    double kb = 0;
                    long bts = 0;
                    if (listening != null) {
                        //To avoid bare line feeds.
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                        listening = listening.replaceAll("\r\n", "\n");
                        listening = listening.replaceAll("\n", "\r\n");
                        if (Variable.smartEncode) {
                            bufferedOutputStream.write(listening.getBytes(privateVariable.encode));
                        } else {
                            bufferedOutputStream.write(listening.getBytes(Variable.defaultEncode));
                        }
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                        bts = (listening.getBytes(privateVariable.encode)).length;
                    } else if (file != null) {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        OutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                        byte[] bytes = new byte[8192];
                        int len = -1;
                        if (privateVariable.getRest() == 0l) {
                            //Not rest mode
                            while ((len = fileInputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, len);
                            }
                            outputStream.flush();
                            fileInputStream.close();
                            outputStream.close();
                            bts = file.length();
                        } else {
                            //Rest mode on
                            fileInputStream.skip(privateVariable.getRest());
                            while ((len = fileInputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, len);
                            }
                            outputStream.flush();
                            fileInputStream.close();
                            outputStream.close();
                            bts = file.length() - privateVariable.getRest();
                            privateVariable.resetRest();
                        }
                    } else if (path != null) {
                        Logger.log(Types.RECV, Levels.DEBUG,"Port mode store. Path: " + path);
                        File file = new File(path);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        if (privateVariable.getRest() == 0l) {
                            boolean deleted = file.delete();
                            Logger.log(Types.RECV, Levels.DEBUG,"The file is already exists but deleted: " + deleted);
                        } else {
                            Logger.log(Types.RECV, Levels.DEBUG,"Continue file receive.");
                        }
                        //FileOutputStream will be create a new file auto.
                        FileOutputStream fileOutputStream = null;
                        try {
                            fileOutputStream = new FileOutputStream(file);
                            InputStream inputStream = socket.getInputStream();
                            byte[] bytes = new byte[8192];
                            int len = -1;
                            while ((len = inputStream.read(bytes)) != -1) {
                                fileOutputStream.write(bytes, 0, len);
                            }
                            fileOutputStream.flush();
                            send.send("226 Transfer complete." + Dict.newLine);
                            inputStream.close();
                            fileOutputStream.close();
                        } catch (FileNotFoundException FNFE) {
                            send.send("550 Permission denied." + Dict.newLine);
                            FNFE.printStackTrace();
                        }
                        privateVariable.resetRest();
                    }
                    if (path != null) {
                        socket.close();
                    } else {
                        kb = bts / 1000;
                        socket.close();
                        long stopTime = System.nanoTime();
                        long nanoEndTime = stopTime - startTime;
                        double endTime = nanoEndTime / 1000000000;
                        double perSecond = 0;
                        if (endTime == 0) {
                            perSecond = kb;
                        } else {
                            perSecond = kb / endTime;
                        }
                        send.send("226 Complete! " + bts + " bytes in " + nanoEndTime + " nanosecond transferred. " + perSecond + " KB/sec.\r\n");
                    }
                }
            } catch (SocketException SE) {
                Logger.log(Types.SYS, Levels.ERROR,"Listening stopped.");
            } catch (IOException IOE) {
                //TODO
                IOE.printStackTrace();
            } catch (Exception E) {
                E.printStackTrace();
            } finally {
                if (pauseListen.isRunning()) {
                    privateVariable.setTimeoutLock(false);
                }
                send = null;
                privateVariable = null;
                pauseListen = null;
                socket = null;
                listening = null;
                file = null;
                Logger.log(Types.SYS, Levels.DEBUG,"PORT Closed.");
            }
        } else {
            privateVariable.setInterrupted(true);
        }
    }

    public void hello(String message) {
        listening = message;
    }

    public void hello(File file) {
        this.file = file;
    }

    public void helloSTOR(String path) {
        this.path = path;
    }

    public void stopSocket() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            Logger.log(Types.SYS, Levels.DEBUG,"Socket on " + socket.getLocalSocketAddress() + "stopped.");
        } catch (IOException IOE) {
            IOE.printStackTrace();
        } catch (NullPointerException NPE) {
            Logger.log(Types.SYS, Levels.WARN, "Latest port mode port not connected. Closing forced.");
        }
    }
}
