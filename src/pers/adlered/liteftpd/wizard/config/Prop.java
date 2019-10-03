package pers.adlered.liteftpd.wizard.config;

import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;

import java.io.*;
import java.util.Properties;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>配置文件读写操作类</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-03 23:45
 **/
public class Prop {
    private static Properties properties = null;

    private static Prop prop = null;

    private Prop() {
        try {
            properties = new Properties();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config.prop"));
            properties.load(bufferedReader);
        } catch (FileNotFoundException FNFE) {
            Logger.log(Types.SYS, Levels.WARN, "Cannot found properties file \"config.prop\" at the root path, re-generating...");
            try {
                File file = new File("config.prop");
                file.createNewFile();
                BufferedReader bufferedReader = new BufferedReader(new FileReader("config.prop"));
                properties.load(bufferedReader);
                // Set default props
                addAnnotation("# ================================================================================================")
                        .addAnnotation("# >>> LiteFTPD-UNIX Configure File")
                        .addAnnotation("# >> Debug level")
                        .addAnnotation("#     Too high level can affect performance!")
                        .addAnnotation("#     0: NONE;")
                        .addAnnotation("#     1: INFO;")
                        .addAnnotation("#     2: WARN && INFO;")
                        .addAnnotation("#     3: ERROR && WARN && INFO;")
                        .addAnnotation("#     4: DEBUG && ERROR && WARN && INFO;")
                        .addAnnotation("# >> maxUserLimit")
                        .addAnnotation("#     Set to 0, will be ignore the limit. Too small value may make multi-thread ftp client not working")
                        .addAnnotation("# >> timeout")
                        .addAnnotation("#     Timeout in second.")
                        .addAnnotation("# >> maxTimeout")
                        .addAnnotation("#     On mode timeout when client is on passive or initiative mode. (default: 21600 sec = 6 hrs)")
                        .addAnnotation("# >> smartEncode")
                        .addAnnotation("#     Smart choose transmission encode.")
                        .addAnnotation("# >> defaultEncode")
                        .addAnnotation("#     Set the default translating encode. Unix is UTF-8, Windows is GB2312.")
                        .addAnnotation("# >> port")
                        .addAnnotation("#     FTP Server listening tcp port.")
                        .addAnnotation("# >> welcomeMessage")
                        .addAnnotation("#     Customize welcome message when user visited.")
                        .addAnnotation("# >> minPort && maxPort")
                        .addAnnotation("#     Appoint passive mode port range.")
                        .addAnnotation("#     Recommend 100+ ports in the range to make sure generation have high-performance!")
                        .addAnnotation("# ================================================================================================")
                        .addAnnotation("# =                                          ↓ CONFIG ↓                                          =")
                        .addAnnotation("# ================================================================================================")
                        .addAnnotation("# ");
                addProperty("debugLevel", "4")
                        .addProperty("maxUserLimit", "100")
                        .addProperty("timeout", "100")
                        .addProperty("maxTimeout", "21600")
                        .addProperty("smartEncode", "true")
                        .addProperty("defaultEncode", "UTF-8")
                        .addProperty("port", "21")
                        .addProperty("welcomeMessage", "This is a demo version.")
                        .addProperty("minPort", "10240")
                        .addProperty("maxPort", "20480");
            } catch (IOException IOE) {
                IOE.printStackTrace();
            }
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }

    public static Prop getInstance() {
        if (prop == null) {
            prop = new Prop();
        }
        return prop;
    }

    private Prop addAnnotation(String annotation) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("config.prop"), true);
            fileOutputStream.write((annotation + "\n").getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException FNFE) {
            FNFE.printStackTrace();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
        return this;
    }

    private Prop addProperty(String key, String value) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("config.prop"), true);
            fileOutputStream.write((key + "=" + value + "\n").getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException FNFE) {
            FNFE.printStackTrace();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
        return this;
    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        getInstance();
    }
}
