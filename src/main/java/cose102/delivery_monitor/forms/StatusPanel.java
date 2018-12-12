package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.models.ParcelStatus;

import javax.swing.*;

class StatusPanel extends JPanel {
    private StatusHeaderPanel headerPanel;
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

    void clear() {
        headerPanel.clear();
        statusJList.setModel(new DefaultListModel<>());
    }

    void refresh(ParcelInformation parcel) {
        if (parcel != null) {
            headerPanel.displayParcelInformation(parcel);
            DefaultListModel<ParcelStatus> model = new DefaultListModel<>();
            parcel.getStatusList().forEach(model::addElement);
            statusJList.setModel(model);
        }
    }

    private JList<ParcelStatus> getStatusJList() {
        JList<ParcelStatus> statusJList = new JList<>();
        statusJList.setCellRenderer(new ParcelStatusRenderer());
        return statusJList;
    }
}
