package pers.adlered.liteftpd.main;

import pers.adlered.liteftpd.analyze.CommandAnalyze;
import pers.adlered.liteftpd.tool.AutoInputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Receive extends Thread {
    private InputStream inputStream;
    private String IPADD;
    private CommandAnalyze commandAnalyze;

    public Receive(InputStream inputStream, String IPADD, CommandAnalyze commandAnalyze) {
        this.inputStream = inputStream;
        this.IPADD = IPADD;
        this.commandAnalyze = commandAnalyze;
    }

    @Override
    public void run() {
        try {
            //READ
            /*
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = bufferedInputStream.read(bytes)) != -1) {
                String command = new String(bytes, 0, length);
                commandAnalyze.analyze(command);
            }
            bufferedInputStream.close();
            */
            /*String newLine = System.getProperty("line.separator");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GB2312"));
            StringBuilder result = new StringBuilder();
            String line; boolean flag = false;
            while ((line = reader.readLine()) != null) {
                String a = "";
                for (byte i : line.getBytes("GB2312")) {
                    a += String.valueOf(i);
                }
                result.append(flag? newLine: "").append(line);
                flag = true;
                System.out.println(line);
                for (byte i : line.getBytes()) {
                    System.out.println(i);
                }
            }*/
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
