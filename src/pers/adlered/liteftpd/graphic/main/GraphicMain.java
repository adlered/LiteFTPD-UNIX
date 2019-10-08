package pers.adlered.liteftpd.graphic.main;

import pers.adlered.liteftpd.dict.Dict;
import pers.adlered.liteftpd.graphic.main.model.MainModels;
import pers.adlered.liteftpd.graphic.update.RunningUpdate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>图形界面初始类</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-10-06 21:55
 **/
public class GraphicMain extends JFrame implements Runnable, ActionListener {
    // Options
    JMenuItem clear = null;

    @Override
    public void run() {
        // Initialize
        setSize(800, 340);
        setLocationRelativeTo(null);
        setTitle("LiteFTPD-UNIX");
        setLayout(new BorderLayout());
        // Panel
        JPanel westPanel = new JPanel(new BorderLayout());
        JPanel eastPanel = new JPanel(new BorderLayout());
        // Console
        MainModels.console.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(MainModels.console);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Menu bar
        JMenuBar jMenuBar = new JMenuBar();
        JMenu action = new JMenu(Dict.actionStr());

        clear = new JMenuItem(Dict.clearStr());
        clear.addActionListener(this);
        action.add(clear);

        jMenuBar.add(action);
        // Data
        MainModels.data.setEditable(false);
        MainModels.console.setLineWrap(true);
        JScrollPane dataPane = new JScrollPane(MainModels.data);
        dataPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dataPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // Add Models
        westPanel.add(scrollPane, BorderLayout.NORTH);
        eastPanel.add(dataPane, BorderLayout.NORTH);
        // Add Panels
        add(jMenuBar, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        // Must the end
        setVisible(true);
        MainModels.guiReady = true;
        // Data update
        Thread dataUpdateThread = new Thread(new RunningUpdate());
        dataUpdateThread.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            MainModels.console.setText("");
        }
    }
}
