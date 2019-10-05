package pers.adlered.liteftpd.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Greetings for users.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class GoodXX {
    public static String getTimeAsWord() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 12) {
            return "morning";
        }
        if (a > 12 && a <= 13) {
            return "noon";
        }
        if (a > 13 && a <= 17) {
            return "afternoon";
        }
        if (a > 17 && a <= 19) {
            return "evening";
        }
        if (a > 19 && a <= 24) {
            return "night";
        }
        return "";
    }
}
