package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelInformation;
import cose102.delivery_monitor.utils.Shortcuts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class ParcelBriefRenderer extends JPanel implements ListCellRenderer<ParcelInformation> {
    private JLabel nameLabel = new JLabel();
    private JLabel dateLabel = new JLabel();
    private JLabel senderLabel = new JLabel();

    ParcelBriefRenderer() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(3, 1));

        this.add(nameLabel);
        this.add(dateLabel);
        this.add(senderLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ParcelInformation> list,
                                                  ParcelInformation parcel, int index, boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText(parcel.getParcelName().equals("") ? "No Title" : parcel.getParcelName());
        dateLabel.setText(Shortcuts.dateTimeToString(parcel.getCreatedAt()));
        senderLabel.setText(parcel.getSenderName());

        JLabel[] labels = { nameLabel, dateLabel, senderLabel };

        nameLabel.setOpaque(true);
        dateLabel.setOpaque(true);
        senderLabel.setOpaque(true);

        nameLabel.setFont(Constants.getFont(Font.BOLD, 15));
        dateLabel.setFont(Constants.getFont());
        senderLabel.setFont(Constants.getFont());

        setCellColor(isSelected ? list.getSelectionBackground() : list.getBackground());

        return this;
    }

    private void setCellColor(Color color) {
        nameLabel.setBackground(color);
        dateLabel.setBackground(color);
        senderLabel.setBackground(color);
        this.setBackground(color);
    }
}