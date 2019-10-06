package pers.adlered.liteftpd.graphic.main;

import pers.adlered.liteftpd.graphic.main.model.MainModels;
import pers.adlered.liteftpd.graphic.update.RunningUpdate;

import javax.swing.*;
import java.awt.*;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>图形界面初始类</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-06 21:55
 **/
public class GraphicMain extends JFrame implements Runnable {
    @Override
    public void run() {
        // Initialize
        setSize(800, 400);
        setLocationRelativeTo(null);
        setTitle("LiteFTPD-UNIX");
        setLayout(new BorderLayout());
        // Panel
        JPanel westPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        // Console
        MainModels.console.setEditable(false);
        MainModels.console.setAutoscrolls(true);
        MainModels.console.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(MainModels.console);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Data
        MainModels.data.setEditable(false);
        // Add Models
        westPanel.add(scrollPane);
        eastPanel.add(MainModels.data);
        // Add Panels
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        // Must the end
        setVisible(true);
        MainModels.guiReady = true;
        // Data update
        Thread dataUpdateThread = new Thread(new RunningUpdate());
        dataUpdateThread.start();
    }
}
