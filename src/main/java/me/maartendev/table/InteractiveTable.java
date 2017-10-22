package me.maartendev.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InteractiveTable {
    private AbstractAction cellButtonClickHandler;
    private ButtonEditor buttonEditor;
    private ButtonRenderer buttonRenderer;
    private JTable table;
    private boolean preventTableRefresh = false;
    private Object[][] data;
    private AbstractAction clickHandler;
    private Object[] columns;

    public InteractiveTable(Object[] columns, Object[][] data,AbstractAction clickHandler) {
        this.columns = columns;
        this.data = data;
        this.clickHandler = clickHandler;
    }

    public JTable getTable() {
        if(this.table == null) table = this.buildTable();
        return this.table;
    }

    public JTable buildTable() {
        JTable table = new JTable(new DefaultTableModel(this.data, this.columns));
        table = addCellEditorAndRenderer(table);

        table.getColumn("Show").setCellRenderer(buttonRenderer);
        table.getColumn("Show").setCellEditor(buttonEditor);

        table.setRowHeight(table.getRowHeight() + 8);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setBackground(Color.WHITE);

        return table;
    }


    private JTable addCellEditorAndRenderer(JTable table) {
        if (cellButtonClickHandler == null) this.cellButtonClickHandler = buildCellButtonClickHandler();
        if (buttonEditor == null) this.buttonEditor = new ButtonEditor(table, cellButtonClickHandler);
        if (buttonRenderer == null) this.buttonRenderer = new ButtonRenderer();

        table.getColumn("Show").setCellRenderer(buttonRenderer);
        table.getColumn("Show").setCellEditor(buttonEditor);

        return table;
    }

    public void setData(Object[][] data) {
        if (preventTableRefresh) {
            return;
        }

        ((DefaultTableModel) table.getModel()).setDataVector(data, this.columns);
        table = addCellEditorAndRenderer(table);
    }


    private AbstractAction buildCellButtonClickHandler() {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                preventTableRefresh = true;

                clickHandler.actionPerformed(e);

                preventTableRefresh = false;
            }
        };
    }
}
