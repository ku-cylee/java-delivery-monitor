package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.models.ParcelInformation;

import javax.swing.*;
import java.util.ArrayList;

class ParcelsPanel extends JPanel {

    ParcelsPanel(MainFrame mainFrame) {
        this.setBounds(0, 0, 450, 600);
        this.setLayout(null);

        JLabel keyLabel = new JLabel("API key");
        keyLabel.setBounds(10, 30, 110, 30);
        this.add(keyLabel);

        JTextField keyTextField = new JTextField();
        keyTextField.setBounds(100, 35, 340, 20);
        this.add(keyTextField);

        try {
            this.add(getParcelsJList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

    private JList<ParcelInformation> getParcelsJList() throws Exception {
        ArrayList<ParcelInformation> parcelList = DatabaseHandler.getInstance().getActiveParcels();
        JList<ParcelInformation> parcelJList = new JList<ParcelInformation>(getJListModel(parcelList));
        parcelJList.setBounds(10, 60, 430, 490);
        parcelJList.setCellRenderer(new ParcelBriefRenderer());
        return parcelJList;
    }

    private DefaultListModel<ParcelInformation> getJListModel(ArrayList<ParcelInformation> parcelList) {
        DefaultListModel<ParcelInformation> model = new DefaultListModel<>();
        parcelList.forEach(model::addElement);
        return model;
    }


}
