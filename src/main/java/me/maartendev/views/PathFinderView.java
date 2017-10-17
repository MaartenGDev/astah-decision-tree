package me.maartendev.views;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IActivity;
import com.change_vision.jude.api.inf.model.IActivityDiagram;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import me.maartendev.datatransformers.NodeRouteDataTransformer;
import me.maartendev.nodes.NodeRoute;
import me.maartendev.pathfinder.ActivityDiagramParser;
import me.maartendev.pathfinder.PathVisualizer;
import me.maartendev.seeders.ColorSeeder;
import me.maartendev.table.ButtonEditor;
import me.maartendev.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Stream;

public class PathFinderView {
    private ActivityDiagramParser activityDiagramParser;
    private NodeRouteDataTransformer nodeRouteDataTransformer;
    private PathVisualizer pathVisualizer;

    public PathFinderView() {
        this.activityDiagramParser = new ActivityDiagramParser();
        this.nodeRouteDataTransformer = new NodeRouteDataTransformer();
        this.pathVisualizer = new PathVisualizer(new ColorSeeder());
    }


    private IActivity getActivityFromDiagram(ActivityDiagramEditor diagramEditor) {
        return ((IActivityDiagram) diagramEditor.getDiagram()).getActivity();

    }

    private ActivityDiagramEditor getCurrentDiagramEditor() {
        try {
            ProjectAccessor project = AstahAPI.getAstahAPI().getProjectAccessor();

            ActivityDiagramEditor diagramEditor = project.getDiagramEditorFactory().getActivityDiagramEditor();
            diagramEditor.setDiagram(project.getProject().getDiagrams()[0]);

            return diagramEditor;

        } catch (ClassNotFoundException | InvalidUsingException | InvalidEditingException | ProjectNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Container getContent() throws InvalidUsingException, InvalidEditingException {
        JTable table = this.buildTable();

        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton("Get uses cases");

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        actionPanel.add(button);

        panel.add(actionPanel, BorderLayout.PAGE_START);
        panel.add(table, BorderLayout.CENTER);

        button.addActionListener(e -> {
            panel.remove(table);
            panel.add(this.buildTable(), BorderLayout.CENTER);
            panel.revalidate();
        });

        panel.revalidate();

        return panel;
    }

    private JTable buildTable() {
        ActivityDiagramEditor diagramEditor = this.getCurrentDiagramEditor();
        IActivity activity = this.getActivityFromDiagram(diagramEditor);
        List<NodeRoute> routes = this.activityDiagramParser.getAllRoutes(activity);

        Object data[][] = this.nodeRouteDataTransformer.getAsObject(routes);

        Object columns[] = {"Type", "Route Ids", "Count", "Show"};

        JTable table = new JTable(new DefaultTableModel(data, columns));


        Action getId = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                boolean hasToShowRoutes = Boolean.parseBoolean(e.getActionCommand());

                String[] routeIdsAsStrings = table.getValueAt(table.getSelectedRow(), table.getColumn("Route Ids").getModelIndex()).toString().split(",");

                int[] routeIds = Stream.of(routeIdsAsStrings).mapToInt(Integer::parseInt).toArray();

                pathVisualizer.toggleRouteByIds(activity, diagramEditor, activityDiagramParser, routes, routeIds, hasToShowRoutes);
            }
        };

        table.getColumn("Show").setCellRenderer(new ButtonRenderer());
        table.getColumn("Show").setCellEditor(new ButtonEditor(table, getId));

        table.setRowHeight(table.getRowHeight() + 8);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setBackground(Color.WHITE);

        return table;
    }
}
