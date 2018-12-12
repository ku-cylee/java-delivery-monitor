package cose102.delivery_monitor.forms;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    public JPanel parcelsPanel;
    public JPanel statusPanel;

    public MainFrame() {
        this.setTitle("Delivery Monitor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(null);

        container.add(parcelsPanel = new ParcelsPanel(this));
        container.add(statusPanel = new StatusPanel(this));

        this.setSize(900, 630);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
