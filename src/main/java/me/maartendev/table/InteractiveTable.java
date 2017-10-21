package me.maartendev.table;

import com.change_vision.jude.api.inf.model.IActivity;
import me.maartendev.datatransformers.NodeRouteDataTransformer;
import me.maartendev.nodes.NodeRoute;
import me.maartendev.pathfinder.ActivityDiagramParser;
import me.maartendev.pathfinder.PathVisualizer;
import me.maartendev.projects.ProjectManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Stream;

public class InteractiveTable {
    private AbstractAction cellButtonClickHandler;
    private ButtonEditor buttonEditor;
    private ButtonRenderer buttonRenderer;
    private JTable pathsTable;
    private boolean preventTableRefresh = true;
    private ProjectManager projectManager;
    private ActivityDiagramParser activityDiagramParser;
    private NodeRouteDataTransformer nodeRouteDataTransformer;
    private PathVisualizer pathVisualizer;
    private AbstractAction clickHandler;
    private Object[] headers;

    public InteractiveTable(ProjectManager projectManager, ActivityDiagramParser activityDiagramParser, NodeRouteDataTransformer nodeRouteDataTransformer, PathVisualizer pathVisualizer, AbstractAction clickHandler) {
        this.projectManager = projectManager;
        this.activityDiagramParser = activityDiagramParser;
        this.nodeRouteDataTransformer = nodeRouteDataTransformer;
        this.pathVisualizer = pathVisualizer;
        this.clickHandler = clickHandler;

        this.pathsTable = this.buildTable();
    }

    public JTable getTable() {
        return this.pathsTable;
    }

    public JTable buildTable() {
        JTable table = new JTable(this.buildTableModel());
        table = addCellEditorAndRenderer(table);

        table.getColumn("Show").setCellRenderer(buttonRenderer);
        table.getColumn("Show").setCellEditor(buttonEditor);

        table.setRowHeight(table.getRowHeight() + 8);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setBackground(Color.WHITE);

        return table;
    }

    private DefaultTableModel buildTableModel() {
        Object data[][] = this.nodeRouteDataTransformer.getAsObject(this.getCurrentRoutes());

        Object columns[] = {"Type", "Route Ids", "Count", "Show"};

        return new DefaultTableModel(data, columns);
    }

    private JTable addCellEditorAndRenderer(JTable table) {
        if (cellButtonClickHandler == null) this.cellButtonClickHandler = buildCellButtonClickHandler();
        if (buttonEditor == null) this.buttonEditor = new ButtonEditor(table, cellButtonClickHandler);
        if (buttonRenderer == null) this.buttonRenderer = new ButtonRenderer();

        table.getColumn("Show").setCellRenderer(buttonRenderer);
        table.getColumn("Show").setCellEditor(buttonEditor);

        return table;
    }

    public void setTableData(Object[][] data) {
        if (preventTableRefresh) {
            return;
        }

        ((DefaultTableModel) pathsTable.getModel()).setDataVector(data, this.headers);
    }

    public void setHeaders(Object[] headers){
        this.headers = headers;
    }

    private List<NodeRoute> getCurrentRoutes() {
        IActivity activity = this.projectManager.getCurrentActivity();

        return activityDiagramParser.getAllRoutes(activity);
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
