package cose102.delivery_monitor.forms;

import javax.swing.*;

public class ParcelsPanel extends JPanel {
    public ParcelsPanel(MainFrame mainFrame) {
        this.setBounds(0, 0, 450, 600);
        this.setLayout(null);

        JButton addButton = new JButton("Add");
        addButton.setBounds(10, 550, 100, 40);
        addButton.setBackground(mainFrame.buttonColor);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(120, 550, 100, 40);
        deleteButton.setBackground(mainFrame.buttonColor);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(230, 550, 100, 40);
        refreshButton.setBackground(mainFrame.buttonColor);
        JButton devButton = new JButton("Developer");
        devButton.setBounds(340, 550, 100, 40);
        devButton.setBackground(mainFrame.buttonColor);

        this.add(addButton);
        this.add(deleteButton);
        this.add(refreshButton);
        this.add(devButton);
    }
}
