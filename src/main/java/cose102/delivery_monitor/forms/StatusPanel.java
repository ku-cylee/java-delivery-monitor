package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelStatus;

import java.util.ArrayList;
import javax.swing.*;

class StatusPanel extends JPanel {
    private JPanel headerPanel;
    private JList<ParcelStatus> statusJList;

    StatusPanel(MainFrame mainFrame) {
        this.setBounds(445, 30, 450, 600);
        this.setLayout(null);

        JScrollPane statusListPane = new JScrollPane(statusJList = getStatusJList());
        statusListPane.setBounds(10, 165, 430, 420);
        statusListPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        statusListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(headerPanel = new StatusHeaderPanel());
        this.add(statusListPane);
    }

    void refresh(ArrayList<ParcelStatus> statusList) {
        DefaultListModel<ParcelStatus> model = new DefaultListModel<>();
        statusList.forEach(model::addElement);
        statusJList.setModel(model);
    }

    private JList<ParcelStatus> getStatusJList() {
        JList<ParcelStatus> statusJList = new JList<>();
        statusJList.setCellRenderer(new ParcelStatusRenderer());
        return statusJList;
    }
}
