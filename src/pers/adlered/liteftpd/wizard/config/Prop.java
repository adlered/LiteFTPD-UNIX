package pers.adlered.liteftpd.wizard.config;

import pers.adlered.liteftpd.logger.Levels;
import pers.adlered.liteftpd.logger.Logger;
import pers.adlered.liteftpd.logger.Types;
import pers.adlered.liteftpd.variable.Variable;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>配置文件读写操作类</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-03 23:45
 **/
public class Prop {
    private static Properties properties = new Properties();

    private static Prop prop = null;

    private Prop() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config.prop"));
            properties.load(bufferedReader);
            Logger.log(Types.SYS, Levels.INFO, "Profile \"config.prop\" loaded successfully.");
        } catch (FileNotFoundException FNFE) {
            Logger.log(Types.SYS, Levels.WARN, "Cannot found properties file \"config.prop\" at the root path, re-generating default...");
            try {
                File file = new File("config.prop");
                file.createNewFile();
                // Set default props
                addAnnotation("# ================================================================================================")
                        .addAnnotation("# ================================================================================================")
                        .addAnnotation("# >>> LiteFTPD-UNIX Configure File")
                        .addAnnotation("# ")
                        .addAnnotation("# >> debugLevel")
                        .addAnnotation("# ")
                        .addAnnotation("#     Too high level can affect performance!")
                        .addAnnotation("#       0: NONE;")
                        .addAnnotation("#       1: INFO;")
                        .addAnnotation("#       2: WARN && INFO;")
                        .addAnnotation("#       3: ERROR && WARN && INFO;")
                        .addAnnotation("#       4: DEBUG && ERROR && WARN && INFO.")
                        .addAnnotation("# ")
                        .addAnnotation("#     Debug等级，调整过高可能会影响性能！")
                        .addAnnotation("#       0：无输出；")
                        .addAnnotation("#       1：输出 INFO 信息；")
                        .addAnnotation("#       2：输出 WARN 及 INFO 信息；")
                        .addAnnotation("#       3：输出 ERROR 、 WARN 及 INFO 信息；")
                        .addAnnotation("#       4：输出 DEBUG 、 ERROR 、 WARN 及 INFO 信息。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> maxUserLimit")
                        .addAnnotation("# ")
                        .addAnnotation("#     Set to 0, will be ignore the limit. Too small value may make multi-thread ftp client not working.")
                        .addAnnotation("# ")
                        .addAnnotation("#     同时连接数限制。设置至0代表不限制。过小的值可能会导致多线程的FTP客户端无法正常工作。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> timeout")
                        .addAnnotation("# ")
                        .addAnnotation("#     Timeout in second.")
                        .addAnnotation("# ")
                        .addAnnotation("#     连接空闲超时时间。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> maxTimeout")
                        .addAnnotation("# ")
                        .addAnnotation("#     On mode timeout when client is on passive or initiative mode. (default: 21600 sec = 6 hrs)")
                        .addAnnotation("# ")
                        .addAnnotation("#     传输时最高的超时时间。（默认：21600秒 = 6小时）")
                        .addAnnotation("# ")
                        .addAnnotation("# >> smartEncode")
                        .addAnnotation("# ")
                        .addAnnotation("#     Smart choose transmission encode.")
                        .addAnnotation("# ")
                        .addAnnotation("#     开启后，LiteFTPD会自动检测编码，以兼容各种系统的FTP客户端。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> defaultEncode")
                        .addAnnotation("# ")
                        .addAnnotation("#     Set the default translating encode. Unix is UTF-8, Windows is GB2312.")
                        .addAnnotation("# ")
                        .addAnnotation("#     设置默认的传输编码。 Unix系统为UTF-8，Windows为GB2312。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> port")
                        .addAnnotation("# ")
                        .addAnnotation("#     FTP Server listening tcp port.")
                        .addAnnotation("# ")
                        .addAnnotation("#     FTP服务监听的TCP端口号。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> welcomeMessage")
                        .addAnnotation("# ")
                        .addAnnotation("#     Customize welcome message when user visited.")
                        .addAnnotation("# ")
                        .addAnnotation("#     自定义用户连接时的欢迎信息。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> minPort && maxPort")
                        .addAnnotation("# ")
                        .addAnnotation("#     Appoint passive mode port range.")
                        .addAnnotation("#       Recommend 100+ ports in the range to make sure generation have high-performance!")
                        .addAnnotation("# ")
                        .addAnnotation("#     自定义被动模式使用的端口范围。")
                        .addAnnotation("#       建议在范围中有100个端口以上，以确保FTP服务端的性能。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> user")
                        .addAnnotation("# ")
                        .addAnnotation("#     Multi users. Format:")
                        .addAnnotation("#       user=[username];[password];[permission];[permitDir];[defaultDir]; ...")
                        .addAnnotation("#       username: User's login name.")
                        .addAnnotation("#       password: User's password.")
                        .addAnnotation("#       permission:")
                        .addAnnotation("#       r = read")
                        .addAnnotation("#       w = write")
                        .addAnnotation("#       d = delete")
                        .addAnnotation("#       c = create")
                        .addAnnotation("#       m = move")
                        .addAnnotation("#       Example: rw means read and write permission.")
                        .addAnnotation("#       permitDir: Set dir that user can access.")
                        .addAnnotation("#       Example: \"/\" means user can access the hole disk; \"/home\" means user can access folder/subFolder/files under \"/home\" directory.")
                        .addAnnotation("#       defaultDir: The default dir will be re-directed when user logged.")
                        .addAnnotation("# ")
                        .addAnnotation("#     多用户管理。格式：")
                        .addAnnotation("#       user=[用户名];[密码];[权限];[允许访问的目录];[登录时的默认目录]; ...")
                        .addAnnotation("#       权限：")
                        .addAnnotation("#       r = 读")
                        .addAnnotation("#       w = 写")
                        .addAnnotation("#       d = 删除")
                        .addAnnotation("#       c = 创建")
                        .addAnnotation("#       m = 移动")
                        .addAnnotation("#       举例：rw 代表读和写的权限。")
                        .addAnnotation("#       允许访问的目录：设置用户可以访问的目录。")
                        .addAnnotation("#       举例：“/”代表用户可以访问整个硬盘；“/home”代表用户可以访问在“/home”目录下的所有子目录、目录和文件。")
                        .addAnnotation("#       登录时的默认目录：登录成功后，用户所在的默认目录。")
                        .addAnnotation("# ")
                        .addAnnotation("# >> ipOnlineLimit")
                        .addAnnotation("# ")
                        .addAnnotation("#     Max connections limit for specify IP Address.")
                        .addAnnotation("#       ipOnlineLimit=[IP];[Limit];[IP];[Limit]; ...")
                        .addAnnotation("#       If you defined IP Address as \"0.0.0.0\", priority will be given to limiting the number of connections per IP address to a specified number (Except for IP Address that have been set up separately).")
                        .addAnnotation("#       \"x\" means \"All\". If you defined \"192.168.x.x\",  that connections from range \"192.168.0-255.0-255\" all will be refused.")
                        .addAnnotation("#       BlackList for Ip Address? Set limit to \"0\"!")
                        .addAnnotation("#       !!! Please note! The higher the configuration, the lower the weight of the connection limit (meaning that the more forward, the less likely it is to match). It is recommended to write the configuration of the specified IP at the end, and write the IP configuration using the wildcard in the front. !!!")
                        .addAnnotation("#       For example, =127.0.0.1; 1; 0.0.0.0; 100; When 127.0.0.1 is connected to the server, the maximum number of simultaneous connections allowed is 100! The configuration should be modified to =0.0.0.0;100;127.0.0.1;1;")
                        .addAnnotation("# ")
                        .addAnnotation("#     限制指定IP地址的最大同时连接数。")
                        .addAnnotation("#       ipOnlineLimit=[IP地址];[最大连接数];[IP地址];[最大连接数]; ...")
                        .addAnnotation("#       如果你将IP地址定义为“0.0.0.0”，服务端将把最大连接数规则应用到所有的IP地址中（除非指定IP地址也被单独定义了）。")
                        .addAnnotation("#       “x”代表“所有”。如果你定义为“192.168.x.x”，那么来自“192.168.0-255.0-255”范围的所有IP地址都将受到该规则的限制。")
                        .addAnnotation("#       想将指定IP地址拉黑？把最大连接数限制为“0”！")
                        .addAnnotation("#       !!! 请注意！配置越往前，连接数限制的权重越低（意味着越往前，匹配到的可能性越小）。建议将指定IP的配置写在最后面，将使用通配符的IP配置写在前面。 !!!")
                        .addAnnotation("#       例如 =127.0.0.1;1;0.0.0.0;100; 当127.0.0.1连接服务端时，最终获取到允许同时的连接数最大为100！应将配置修改为 =0.0.0.0;100;127.0.0.1;1;")
                        .addAnnotation("# ")
                        .addAnnotation("# >> userOnlineLimit")
                        .addAnnotation("# ")
                        .addAnnotation("#     Max connections limit for specify User.")
                        .addAnnotation("#       userOnlineLimit=[username];[Limit];[username];[Limit]; ...")
                        .addAnnotation("#       If you defined User as \"%\", priority will be given to limiting the number of connections per User to a specified number (Except for users that have been set up separately).")
                        .addAnnotation("#       BlackList for User? Set limit to \"0\"!")
                        .addAnnotation("#       !!! Please note! The higher the configuration, the lower the weight of the connection limit (meaning that the more forward, the less likely it is to match). It is recommended to write the configuration of the specified user to the end, and write the user configuration using the wildcard to the front. !!!")
                        .addAnnotation("#       For example, =admin;1;%;100; When logging in to the user admin, the maximum number of connections allowed to log in at the same time is 100! The configuration should be modified to =%;100;admin;1;")
                        .addAnnotation("# ")
                        .addAnnotation("#     限制指定用户的最大同时连接数。")
                        .addAnnotation("#       userOnlineLimit=[用户名];[最大连接数];[用户名];[最大连接数]; ...")
                        .addAnnotation("#       如果你将用户名定义为“%”，服务端讲把最大连接数规则应用到所有的用户中（除非指定用户也被单独定义了）。")
                        .addAnnotation("#       想将指定用户拉黑？把最大连接数限制为“0”！")
                        .addAnnotation("#       !!! 请注意！配置越往前，连接数限制的权重越低（意味着越往前，匹配到的可能性越小）。建议将指定用户的配置写在最后面，将使用通配符的用户配置写在前面。 !!!")
                        .addAnnotation("#       例如 =admin;1;%;100; 当登录用户admin时，最终获取到允许同时登录的连接数最大为100！应将配置修改为 =%;100;admin;1;")
                        .addAnnotation("# ")
                        .addAnnotation("# ================================================================================================")
                        .addAnnotation("# =                                          ↓ CONFIG ↓                                          =")
                        .addAnnotation("# ================================================================================================")
                        .addAnnotation("# ");
                addProperty("user", "anonymous;;r;/;/;admin;123456;r;/;/root;")
                        .addProperty("ipOnlineLimit", "0.0.0.0;100;127.x.x.x;100;192.168.1.x;100;")
                        .addProperty("userOnlineLimit", "%;100;anonymous;100;admin;100;")
                        .addProperty("debugLevel", "3")
                        .addProperty("maxUserLimit", "100")
                        .addProperty("timeout", "100")
                        .addProperty("maxTimeout", "21600")
                        .addProperty("smartEncode", "true")
                        .addProperty("defaultEncode", "UTF-8")
                        .addProperty("port", "21")
                        .addProperty("welcomeMessage", "オレは ルフィ、海賊王になる男だ")
                        .addProperty("minPort", "10240")
                        .addProperty("maxPort", "20480");
                BufferedReader bufferedReader = new BufferedReader(new FileReader("config.prop"));
                properties.load(bufferedReader);
            } catch (IOException IOE) {
                IOE.printStackTrace();
            }
        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
        // 反射并应用配置
        try {
            Class clazz = Variable.class;
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                Field field = clazz.getDeclaredField(key.toString());
                switch (field.getType().toString()) {
                    case "int":
                        field.set(clazz, Integer.parseInt(getProperty(key.toString())));
                        break;
                    case "long":
                        field.set(clazz, Long.parseLong(getProperty(key.toString())));
                        break;
                    case "boolean":
                        field.set(clazz, Boolean.parseBoolean(getProperty(key.toString())));
                        break;
                    case "class java.lang.String":
                        field.set(clazz, getProperty(key.toString()));
                        break;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
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
}
