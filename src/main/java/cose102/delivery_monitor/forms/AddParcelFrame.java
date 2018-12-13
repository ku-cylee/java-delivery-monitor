package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.db_handler.DatabaseHandler;
import cose102.delivery_monitor.models.Company;
import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.utils.ParcelRetriever;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

class AddParcelFrame extends JFrame {
    private JComboBox<Company> companyComboBox;
    private JTextField invoiceTextField;

    AddParcelFrame(MainFrame mainFrame) {
        this.setTitle("Add Parcel");

        Container container = this.getContentPane();
        container.setLayout(null);

        ArrayList<Company> companyList = new ArrayList<>();

        try {
            companyList = DatabaseHandler.getInstance().getCompanyList();
        } catch (Exception e) { }

        if (companyList.size() > 0) {
            JLabel companyLabel = new JLabel("Company");
            companyLabel.setBounds(20, 10, 80, 30);
            companyLabel.setFont(Constants.getFont());


            companyComboBox = new JComboBox<Company>(getModel(companyList));
            companyComboBox.setRenderer(new CompanyComboBoxRenderer());
            companyComboBox.setBounds(120, 10, 260, 30);
            companyComboBox.setFont(Constants.getFont());

            JLabel invoiceLabel = new JLabel("Invoice No.");
            invoiceLabel.setBounds(20, 45, 80, 30);
            invoiceLabel.setFont(Constants.getFont());

            invoiceTextField = new JTextField();
            invoiceTextField.setBounds(120, 45, 260, 30);
            invoiceTextField.setFont(Constants.getFont());

            JButton addButton = new JButton("Add");
            addButton.setBounds(90, 80, 100, 30);
            addButton.setBackground(Constants.BUTTON_COLOR);
            addButton.setFont(Constants.getFont());
            addButton.addActionListener((e) -> {
                ParcelRetriever retriever = new ParcelRetriever(mainFrame.parcelsPanel.keyTextField.getText());
                String companyCode = companyComboBox.getItemAt(companyComboBox.getSelectedIndex()).getCompanyCode();
                try {
                    ParcelInformation parcel = retriever.getParcelInformation(companyCode, invoiceTextField.getText());
                    DatabaseHandler.getInstance().insertParcelInformation(parcel);
                    mainFrame.parcelsPanel.refresh();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, "Parcel data couldn't be loaded.");
                } finally {
                    close();
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBounds(210, 80, 100, 30);
            cancelButton.setBackground(Constants.BUTTON_COLOR);
            cancelButton.setFont(Constants.getFont());
            cancelButton.addActionListener((e) -> close());

            container.add(companyLabel);
            container.add(companyComboBox);
            container.add(invoiceLabel);
            container.add(invoiceTextField);

            container.add(addButton);
            container.add(cancelButton);

            this.setResizable(false);
            this.setSize(400, 150);
            this.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Company data is not loaded");
        }
    }

    private DefaultComboBoxModel<Company> getModel(ArrayList<Company> companyList) {
        DefaultComboBoxModel<Company> model = new DefaultComboBoxModel<>();
        companyList.forEach(model::addElement);
        return model;
    }

    private void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public Insets getInsets() {
        return new Insets(25, 0, 0, 0);
    }
}
