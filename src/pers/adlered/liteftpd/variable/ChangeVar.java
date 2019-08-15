package pers.adlered.liteftpd.variable;

public class ChangeVar {
    public static void plusOnlineCount() {
        ++Variable.online;
        System.out.println("Online: " + Variable.online);
    }

    public static void reduceOnlineCount() {
        --Variable.online;
        System.out.println("Online: " + Variable.online);
    }
}
