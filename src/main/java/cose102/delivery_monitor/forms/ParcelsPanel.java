package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.models.ParcelInformation;

import javax.swing.*;
import java.util.ArrayList;

class ParcelsPanel extends JPanel {
    MainFrame mainFrame;
    JTextField keyTextField;
    JList<ParcelInformation> parcelJList;

    ParcelsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setBounds(5, 30, 450, 600);
        this.setLayout(null);

        JLabel keyLabel = new JLabel("API key");
        keyLabel.setBounds(10, 0, 110, 30);
        this.add(keyLabel);

        keyTextField = new JTextField();
        keyTextField.setBounds(100, 5, 340, 20);
        this.add(keyTextField);

        JScrollPane parcelListPane = new JScrollPane(parcelJList = getParcelsJList());
        parcelListPane.setBounds(10, 30, 430, 515);
        parcelListPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        parcelListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        parcelJList.addListSelectionListener((e) -> {
            ParcelInformation parcel = ((JList<ParcelInformation>)e.getSource()).getSelectedValue();
            mainFrame.statusPanel.refresh(parcel.getStatusList());
        });
        this.add(parcelListPane);

        JButton addButton = createButton("Add", 10);
        JButton deleteButton = createButton("Delete", 120);
        JButton refreshButton = createButton("Refresh", 230);
        JButton devButton = createButton("Developer", 340);

        this.add(addButton);
        this.add(deleteButton);
        this.add(refreshButton);
        this.add(devButton);
    }

    private JList<ParcelInformation> getParcelsJList() {
        try {
            ArrayList<ParcelInformation> parcelList = DatabaseHandler.getInstance().getActiveParcels();
            JList<ParcelInformation> parcelJList = new JList<>(getJListModel(parcelList));
            parcelJList.setCellRenderer(new ParcelBriefRenderer());
            return parcelJList;
        } catch (Exception e) { return null; }
    }

    private DefaultListModel<ParcelInformation> getJListModel(ArrayList<ParcelInformation> parcelList) {
        DefaultListModel<ParcelInformation> model = new DefaultListModel<>();
        parcelList.forEach(model::addElement);
        return model;
    }

    private JButton createButton(String text, int xCoordinate) {
        JButton button = new JButton(text);
        button.setBounds(xCoordinate, 555, 100, 30);
        button.setBackground(Constants.BUTTON_COLOR);
        return button;
    }
}
