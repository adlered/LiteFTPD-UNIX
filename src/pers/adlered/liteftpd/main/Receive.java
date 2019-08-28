package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.analyze.PrivateVariable;
import pers.adlered.liteftpd.bind.IPAddressBind;
import pers.adlered.liteftpd.tool.AutoInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Receive extends Thread {
    private InputStream inputStream = null;
    private CommandAnalyze commandAnalyze = null;
    private PauseListen pauseListen = null;
    private PrivateVariable privateVariable = null;
    private IPAddressBind ipAddressBind = null;

    public Receive(InputStream inputStream, CommandAnalyze commandAnalyze, PauseListen pauseListen, PrivateVariable privateVariable, IPAddressBind ipAddressBind) {
        this.inputStream = inputStream;
        this.commandAnalyze = commandAnalyze;
        this.pauseListen = pauseListen;
        this.privateVariable = privateVariable;
        this.ipAddressBind = ipAddressBind;
    }

    @Override
    public void run() {
        try {
            //READ
            AutoInputStream autoInputStream = new AutoInputStream(inputStream, 1024, privateVariable);
            while (true) {
                String autoLine = autoInputStream.readLineAuto();
                if (!pauseListen.isRunning()) {
                    System.out.println("Receive stopped.");
                    break;
                }
                System.out.println(ipAddressBind.getIPADD() + "=>" + ipAddressBind.getSRVIPADD() + ": " + autoLine.replaceAll("\r|\n", ""));
                commandAnalyze.analyze(autoLine);
            }
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }


}
