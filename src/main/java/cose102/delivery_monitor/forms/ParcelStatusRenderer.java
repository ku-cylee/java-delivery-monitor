package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.ParcelStatus;
import cose102.delivery_monitor.utils.Shortcuts;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class ParcelStatusRenderer extends JPanel implements ListCellRenderer<ParcelStatus> {
    private JLabel dateLabel = new JLabel();
    private JLabel statusLabel = new JLabel();

    ParcelStatusRenderer() {
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setLayout(new GridLayout(2, 1));

        this.add(dateLabel);
        this.add(statusLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ParcelStatus> list,
                     ParcelStatus status, int index, boolean isSelected, boolean cellHasFocus) {
        dateLabel.setText(Shortcuts.dateTimeToString(status.getStatusTime()));
        statusLabel.setText(String.format("[%s] %s", status.getCategory(), status.getLocation()));

        dateLabel.setOpaque(true);
        statusLabel.setOpaque(true);

        dateLabel.setFont(Constants.getFont());
        statusLabel.setFont(Constants.getFont());

        setCellColor(isSelected ? Constants.SELECTED_COLOR : Constants.UNSELECTED_COLOR);

        return this;
    }

    private void setCellColor(Color color) {
        dateLabel.setBackground(color);
        statusLabel.setBackground(color);
        this.setBackground(color);
    }
}
