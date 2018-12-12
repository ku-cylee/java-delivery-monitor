package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.utils.Shortcuts;

import java.awt.*;
import javax.swing.*;

class StatusHeaderPanel extends JPanel {
    private JLabel parcelNameLabel = new JLabel();
    private JLabel createdAtLabel = new JLabel();
    private JLabel invoiceLabel = new JLabel();
    private JLabel senderNameLabel = new JLabel();
    private JLabel receiverNameLabel = new JLabel();
    private JLabel receiverAddressLabel = new JLabel();
    private JLabel completedLabel = new JLabel();

    StatusHeaderPanel() {
        this.setBounds(10, 5, 430, 150);
        this.setOpaque(true);
        this.setBackground(Color.WHITE);

        this.setLayout(new GridLayout(7, 1));

        JLabel[] labels = { createdAtLabel, invoiceLabel, senderNameLabel,
                            receiverNameLabel, receiverAddressLabel, completedLabel };

        parcelNameLabel.setFont(Constants.getFont(Font.BOLD, 15));
        for(JLabel label:labels) label.setFont(Constants.getFont());

        this.add(parcelNameLabel);
        for(JLabel label:labels) this.add(label);
    }

    void displayParcelInformation(ParcelInformation parcel) {
        parcelNameLabel.setText(parcel.getParcelName());
        createdAtLabel.setText(Shortcuts.dateTimeToString(parcel.getCreatedAt()));
        invoiceLabel.setText(String.format("%s | %s", parcel.getCompany().getCompanyName(), parcel.getInvoiceNumber()));
        senderNameLabel.setText(parcel.getSenderName());
        receiverNameLabel.setText(parcel.getReceiverName());
        receiverAddressLabel.setText(parcel.getReceiverAddress());
        completedLabel.setText(parcel.isCompleted() ? "Completed" : "Incomplete");
    }
}
