package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.tool.AutoInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Receive extends Thread {
    private InputStream inputStream = null;
    private String IPADD = null;
    private CommandAnalyze commandAnalyze = null;

    public Receive(InputStream inputStream, String IPADD, CommandAnalyze commandAnalyze) {
        this.inputStream = inputStream;
        this.IPADD = IPADD;
        this.commandAnalyze = commandAnalyze;
    }

    @Override
    public void run() {
        try {
            //READ
            AutoInputStream autoInputStream = new AutoInputStream(inputStream, 1024);
            while (true) {
                String autoLine = autoInputStream.readLineAuto();
                commandAnalyze.analyze(autoLine);
            }
        } catch (IOException IOE) {
            //TODO
            IOE.printStackTrace();
        }
    }


}
