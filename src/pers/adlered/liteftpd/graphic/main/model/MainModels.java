package pers.adlered.liteftpd.graphic.main.model;

import javax.swing.*;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>存放主界面控件</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-06 22:14
 **/
public class MainModels {
    public static boolean guiReady = false;

    public static JTextArea console = new JTextArea(18, 31);
    public static JTextArea data = new JTextArea(3, 32);
}
