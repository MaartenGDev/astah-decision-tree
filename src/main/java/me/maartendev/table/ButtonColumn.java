package me.maartendev.table;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
    private JTable table;
    private Action action;
    private Border originalBorder;
    private Border focusBorder;

    private JButton renderButton;
    private JButton editButton;
    private Object editorValue;
    private boolean isButtonColumnEditor;

    private List<JButton> actionButtons = new ArrayList<>();

    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;

        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        originalBorder = editButton.getBorder();

        setFocusBorder(new LineBorder(Color.BLUE));
        TableColumn modelColumn = table.getColumnModel().getColumn(column);
        modelColumn.setCellRenderer(this);
        modelColumn.setCellEditor(this);

        table.addMouseListener(this);
    }

    private void setFocusBorder(Border focusBorder) {
        this.focusBorder = focusBorder;
        editButton.setBorder(focusBorder);
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.editorValue = "test";
        JButton btn = new JButton("Toggle");

        return getButtonWithValue(btn, "test");
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        renderButton.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        renderButton.setBackground(Color.WHITE);

        renderButton.setBorder(hasFocus ? focusBorder : originalBorder);

        JButton btn = new JButton("Toggle");
        actionButtons.add(btn);

        return getButtonWithValue(btn, "Toggle");
    }

    private JButton getButtonWithValue(JButton button, Object value) {
        if (value == null) {
            button.setText("");
            button.setIcon(null);

            return renderButton;
        } else if (value instanceof Icon) {
            button.setText("");
            button.setIcon((Icon) value);

            return button;
        }

        button.setText(value.toString());
        button.setIcon(null);

        return button;
    }


    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();

        action.actionPerformed(new ActionEvent(table,ActionEvent.ACTION_PERFORMED,"" + row));
    }

    public void mousePressed(MouseEvent e) {
        if (table.isEditing() && table.getCellEditor() == this) {
            isButtonColumnEditor = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isButtonColumnEditor && table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        isButtonColumnEditor = false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}