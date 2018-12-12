package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.models.ParcelInformation;

import javax.swing.*;
import java.util.ArrayList;

class ParcelsPanel extends JPanel {
    MainFrame mainFrame;
    JTextField keyTextField;
    private JList<ParcelInformation> parcelJList;

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

        parcelJList = new JList<>();
        parcelJList.setCellRenderer(new ParcelBriefRenderer());
        parcelJList.addListSelectionListener((e) -> {
            ParcelInformation parcel = parcelJList.getSelectedValue();
            mainFrame.statusPanel.refresh(parcel);
        });
        parcelJList.setModel(getModel());

        JScrollPane parcelListPane = new JScrollPane(parcelJList);
        parcelListPane.setBounds(10, 30, 430, 515);
        parcelListPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        parcelListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(parcelListPane);

        JButton addButton = createButton("Add", 10);
        JButton deleteButton = createButton("Delete", 120);
        JButton refreshButton = createButton("Refresh", 230);
        JButton devButton = createButton("Developer", 340);

        addButton.addActionListener((e) -> { new AddParcelFrame(mainFrame); });
        deleteButton.addActionListener((e) -> {
            int deleteChoice = JOptionPane.showConfirmDialog(null, "Really delete selected parcel?",
                                                             "Really Delete?", JOptionPane.YES_NO_OPTION);
            if (deleteChoice == JOptionPane.YES_OPTION) {
                try {
                    ParcelInformation parcel = parcelJList.getSelectedValue();
                    DatabaseHandler.getInstance().disableParcel(parcel.getId());
                    refresh();
                } catch (Exception exception) { };
            }
        });

        this.add(addButton);
        this.add(deleteButton);
        this.add(refreshButton);
        this.add(devButton);
    }

    private DefaultListModel<ParcelInformation> getModel() {
        try {
            ArrayList<ParcelInformation> parcelList = DatabaseHandler.getInstance().getActiveParcels();
            DefaultListModel<ParcelInformation> model = new DefaultListModel<>();
            parcelList.forEach(model::addElement);
            return model;
        } catch (Exception e) { return null; }
    }

    private JButton createButton(String text, int xCoordinate) {
        JButton button = new JButton(text);
        button.setBounds(xCoordinate, 555, 100, 30);
        button.setBackground(Constants.BUTTON_COLOR);
        button.setFont(Constants.getFont());
        return button;
    }

    void refresh() {
        parcelJList.setModel(getModel());
    }
}
