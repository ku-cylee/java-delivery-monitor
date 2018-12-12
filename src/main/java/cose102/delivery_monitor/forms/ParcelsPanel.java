package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.models.ParcelInformation;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

class ParcelsPanel extends JPanel {
    MainFrame mainFrame;
    JTextField keyTextField;

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

        JList<ParcelInformation> parcelJList = getParcelsJList();
        parcelJList.addListSelectionListener((e) -> {
            ParcelInformation parcel = ((JList<ParcelInformation>)e.getSource()).getSelectedValue();
        });
        this.add(parcelJList);

        JButton addButton = new JButton("Add");
        addButton.setBounds(10, 555, 100, 30);
        addButton.setBackground(Constants.BUTTON_COLOR);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(120, 555, 100, 30);
        deleteButton.setBackground(Constants.BUTTON_COLOR);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(230, 555, 100, 30);
        refreshButton.setBackground(Constants.BUTTON_COLOR);
        JButton devButton = new JButton("Developer");
        devButton.setBounds(340, 555, 100, 30);
        devButton.setBackground(Constants.BUTTON_COLOR);

        this.add(addButton);
        this.add(deleteButton);
        this.add(refreshButton);
        this.add(devButton);
    }

    private JList<ParcelInformation> getParcelsJList() {
        try {
            ArrayList<ParcelInformation> parcelList = DatabaseHandler.getInstance().getActiveParcels();
            JList<ParcelInformation> parcelJList = new JList<ParcelInformation>(getJListModel(parcelList));
            parcelJList.setBounds(10, 30, 430, 515);
            parcelJList.setCellRenderer(new ParcelBriefRenderer());
            return parcelJList;
        } catch (Exception e) { return null; }
    }

    private DefaultListModel<ParcelInformation> getJListModel(ArrayList<ParcelInformation> parcelList) {
        DefaultListModel<ParcelInformation> model = new DefaultListModel<>();
        parcelList.forEach(model::addElement);
        return model;
    }
}
