package cose102.delivery_monitor.forms;

import cose102.delivery_monitor.models.Company;

import javax.swing.*;
import java.awt.*;

class CompanyComboBoxRenderer extends JLabel implements ListCellRenderer<Company> {
    CompanyComboBoxRenderer() {
        this.setFont(Constants.getFont());
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Company> list,
                     Company company, int index, boolean isSelected, boolean cellHasFocus) {
        this.setText(company.getCompanyName());

        setCellColor(isSelected ? Constants.SELECTED_COLOR : Constants.UNSELECTED_COLOR);
        return this;
    }

    private void setCellColor(Color color) {
        this.setBackground(color);
    }
}
