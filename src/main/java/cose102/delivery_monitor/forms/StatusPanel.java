package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelStatus;

import java.util.ArrayList;
import javax.swing.*;

class StatusPanel extends JPanel {
    MainFrame mainFrame;
    JPanel headerPanel;
    JList<ParcelStatus> statusJList;

    StatusPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setBounds(445, 30, 450, 600);
        this.setLayout(null);

        this.add(headerPanel = new StatusHeaderPanel());
        this.add(statusJList = getStatusJList());
    }

    void refresh(ArrayList<ParcelStatus> statusList) {
        statusJList.setModel(getJListModel(statusList));
    }

    private JList<ParcelStatus> getStatusJList() {
        JList<ParcelStatus> statusJList = new JList<>();
        statusJList.setBounds(10, 165, 430, 420);
        statusJList.setCellRenderer(new ParcelStatusRenderer());
        return statusJList;
    }

    private DefaultListModel<ParcelStatus> getJListModel(ArrayList<ParcelStatus> statusList) {
        DefaultListModel<ParcelStatus> model = new DefaultListModel<>();
        statusList.forEach(model::addElement);
        return model;
    }
}
