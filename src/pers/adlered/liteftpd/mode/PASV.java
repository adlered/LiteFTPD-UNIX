package pers.adlered.liteftpd.mode;

import org.omg.CORBA.SystemException;
import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.main.PauseListen;
import pers.adlered.liteftpd.main.Send;
import pers.adlered.liteftpd.variable.Variable;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class PASV extends Thread {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private Send send = null;
    private PrivateVariable privateVariable = null;
    private PauseListen pauseListen = null;

    private String listening = null;
    private File file = null;
    private String path = null;

    public PASV(Send send, PrivateVariable privateVariable, PauseListen pauseListen) {
        this.send = send;
        this.privateVariable = privateVariable;
        this.pauseListen = pauseListen;
    }

    public boolean listen(int port) {
        boolean result = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            //System.out.println("Listening " + port + "...");
            this.serverSocket = serverSocket;
        } catch (BindException BE) {
            result = false;
        } catch (IOException IOE) {
            result = false;
        }
        return result;
    }

    @Override
    public void run() {
        try {
            System.out.println("Transmitter is waiting the port " + serverSocket.getLocalPort() + " for the client.");
            Socket socket = serverSocket.accept();
            this.socket = socket;
            //System.out.println("Connected. Waiting for " + socket.getRemoteSocketAddress() + "...");
            try {
                while (listening == null && file == null && path == null) {
                    if (!pauseListen.isRunning()) {
                        System.out.println("Passive mode listener paused.");
                        break;
                    }
                    Thread.sleep(5);
                }
            } catch (InterruptedException IE) {
            }
            privateVariable.setTimeoutLock(true);
            if (pauseListen.isRunning()) {
                //System.out.println("Service has response.");
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
                            //System.out.println("Length: " + len);
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
                            //System.out.println("Length: " + len);
                            outputStream.write(bytes, 0, len);
                        }
                        outputStream.flush();
                        fileInputStream.close();
                        outputStream.close();
                        bts = file.length() - privateVariable.getRest();
                        privateVariable.resetRest();
                    }
                } else if (path != null) {
                    System.out.println("PASV STOR. PATH: " + path);
                    File file = new File(path);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (privateVariable.getRest() == 0l) {
                        boolean deleted = file.delete();
                        System.out.println("Already exists and deleted: " + deleted);
                    } else {
                        System.out.println("Continue receive.");
                    }
                    //FileOutputStream will be create a new file auto.
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        InputStream inputStream = socket.getInputStream();
                        byte[] bytes = new byte[8192];
                        int len = -1;
                        while ((len = inputStream.read(bytes)) != -1) {
                            //System.out.println("Length: " + len);
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
                    serverSocket.close();
                } else {
                    kb = bts / 1000;
                    socket.close();
                    serverSocket.close();
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
            //System.out.println("Listening stopped.");
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        } catch (Exception E) {
            //System.out.println("err");
            E.printStackTrace();
        } finally {
            if (pauseListen.isRunning()) {
                privateVariable.setTimeoutLock(false);
            }
            send = null;
            privateVariable = null;
            pauseListen = null;
            serverSocket = null;
            socket = null;
            listening = null;
            file = null;
            //System.out.println("PASV Closed.");
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
            serverSocket.close();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            //System.out.println("Server socket on " + serverSocket.getLocalSocketAddress() + "stopped.");
        } catch (IOException IOE) {
            IOE.printStackTrace();
        } catch (NullPointerException NPE) {
            //System.out.println("Latest passive port not connected. Closing forced.");
        }
    }
}
