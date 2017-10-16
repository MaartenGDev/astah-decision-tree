package me.maartendev.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean isActive;

    private JTable table;
    private Action action;

    private boolean stateIsOn = false;
    private String label;


    public ButtonEditor(JTable table, Action action) {
        super(new JCheckBox());
        this.table = table;
        this.action = action;

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
//        button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        label = stateIsOn ? "Show" : "Hide";
        button.setText(label);
        isActive = true;
        return button;
    }

    public Object getCellEditorValue() {
        stateIsOn = !stateIsOn;

        if (isActive) {
            action.actionPerformed(new ActionEvent(table,ActionEvent.ACTION_PERFORMED,stateIsOn ? "true" : "false"));
        }

        isActive = false;
        return label;
    }

    public boolean stopCellEditing() {
        isActive = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

