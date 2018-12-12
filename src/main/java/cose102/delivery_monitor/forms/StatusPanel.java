package cose102.delivery_monitor.forms;

import java.awt.*;
import javax.swing.*;

class StatusPanel extends JPanel {
    MainFrame mainFrame;

    StatusPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setBounds(445, 30, 450, 600);
        this.setLayout(null);

        JPanel headerPanel = new StatusHeaderPanel();

        this.add(headerPanel);
    }
}
