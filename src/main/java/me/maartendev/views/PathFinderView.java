package me.maartendev.views;

import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.model.IActivity;
import me.maartendev.datatransformers.NodeRouteDataTransformer;
import me.maartendev.nodes.ActivityNodeTreeBuilder;
import me.maartendev.nodes.NodeRoute;
import me.maartendev.pathfinder.ActivityDiagramParser;
import me.maartendev.pathfinder.PathVisualizer;
import me.maartendev.projects.ProjectManager;
import me.maartendev.seeders.ColorSeeder;
import me.maartendev.table.InteractiveTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Stream;

public class PathFinderView {
    private ActivityDiagramParser activityDiagramParser;
    private PathVisualizer pathVisualizer;
    private InteractiveTable testCaseTable;
    private ProjectManager projectManager;
    private NodeRouteDataTransformer nodeRouteDataTransformer;

    public PathFinderView() {
        this.nodeRouteDataTransformer = new NodeRouteDataTransformer();
        this.activityDiagramParser = new ActivityDiagramParser();
        this.pathVisualizer = new PathVisualizer(new ColorSeeder());
        this.projectManager = new ProjectManager();

        this.testCaseTable = new InteractiveTable(new String[]{"Type", "Route Ids", "Count", "Show"}, getFreshTableData(), buildCellButtonClickHandler());


    }

    public void clearDiagramAnnotations() {
        ActivityDiagramEditor diagramEditor = this.projectManager.getCurrentDiagramEditor();
        IActivity activity = this.projectManager.getCurrentActivity();
        List<NodeRoute> routes = this.activityDiagramParser.getAllRoutes(activity);
        int[] routeIds = routes.stream().mapToInt(x -> x.id).toArray();

        pathVisualizer.toggleRouteByIds(activity, diagramEditor, activityDiagramParser, routes, routeIds, false);
    }

    private List<NodeRoute> getCurrentRoutes(){
        IActivity activity = this.projectManager.getCurrentActivity();
        return this.activityDiagramParser.getAllRoutes(activity);
    }

    public Container loadContent() {
        this.clearDiagramAnnotations();

        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton("Get uses cases");

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        actionPanel.add(button);

        panel.add(actionPanel, BorderLayout.PAGE_START);
        panel.add(this.testCaseTable.getTable(), BorderLayout.CENTER);

        button.addActionListener(e -> {
            this.activityDiagramParser.getScenarios(projectManager.getCurrentActivity());
        });

        return panel;
    }

    private Object[][] getFreshTableData(){
        return this.nodeRouteDataTransformer.getAsObject(this.getCurrentRoutes());
    }

    public void loadFreshTableData() {
        this.testCaseTable.setData(getFreshTableData());
    }

    private AbstractAction buildCellButtonClickHandler() {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                IActivity activity = projectManager.getCurrentActivity();
                List<NodeRoute> routes = getCurrentRoutes();

                JTable table = testCaseTable.getTable();

                boolean hasToShowRoutes = Boolean.parseBoolean(e.getActionCommand());

                String[] routeIdsAsStrings = table.getValueAt(table.getSelectedRow(), table.getColumn("Route Ids").getModelIndex()).toString().split(",");

                int[] routeIds = Stream.of(routeIdsAsStrings).mapToInt(Integer::parseInt).toArray();

                pathVisualizer.toggleRouteByIds(activity, projectManager.getCurrentDiagramEditor(), activityDiagramParser, routes, routeIds, hasToShowRoutes);
            }
        };
    }
}
