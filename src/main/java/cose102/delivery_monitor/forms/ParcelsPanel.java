package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.models.ParcelInformation;

import javax.swing.*;
import java.util.ArrayList;

class ParcelsPanel extends JPanel {

    ParcelsPanel(MainFrame mainFrame) {
        this.setBounds(0, 0, 450, 600);
        this.setLayout(null);

        try {
            this.add(getParcelsJList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JButton addButton = new JButton("Add");
        addButton.setBounds(10, 550, 100, 40);
        addButton.setBackground(Constants.buttonColor);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(120, 550, 100, 40);
        deleteButton.setBackground(Constants.buttonColor);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(230, 550, 100, 40);
        refreshButton.setBackground(Constants.buttonColor);
        JButton devButton = new JButton("Developer");
        devButton.setBounds(340, 550, 100, 40);
        devButton.setBackground(Constants.buttonColor);

        this.add(addButton);
        this.add(deleteButton);
        this.add(refreshButton);
        this.add(devButton);
    }

    private JList<ParcelInformation> getParcelsJList() throws Exception {
        ArrayList<ParcelInformation> parcelList = DatabaseHandler.getInstance().getAllParcelInformation();
        JList<ParcelInformation> parcelJList = new JList<ParcelInformation>(getJListModel(parcelList));
        parcelJList.setBounds(10, 50, 430, 490);
        return parcelJList;
    }

    private DefaultListModel<ParcelInformation> getJListModel(ArrayList<ParcelInformation> parcelList) {
        DefaultListModel<ParcelInformation> model = new DefaultListModel<>();
        parcelList.forEach(model::addElement);
        return model;
    }
}
