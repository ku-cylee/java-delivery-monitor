package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelInformation;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setTitle("Example Frame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(null);

        container.add(new ParcelsPanel(this));

        this.setSize(900, 600);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
