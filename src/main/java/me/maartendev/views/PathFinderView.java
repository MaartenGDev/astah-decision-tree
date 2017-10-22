package me.maartendev.views;

import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.model.IActivity;
import me.maartendev.datatransformers.NodeRouteDataTransformer;
import me.maartendev.datatransformers.ScenariosNodeRouteDataTransformer;
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
    private ScenariosNodeRouteDataTransformer scenariosNodeRouteDataTransformer;
    private ActivityDiagramParser activityDiagramParser;
    private PathVisualizer pathVisualizer;
    private ProjectManager projectManager;
    private NodeRouteDataTransformer nodeRouteDataTransformer;

    private InteractiveTable testCaseTable;
    private InteractiveTable scenariosTable;

    public PathFinderView() {
        this.nodeRouteDataTransformer = new NodeRouteDataTransformer();
        this.scenariosNodeRouteDataTransformer = new ScenariosNodeRouteDataTransformer();

        this.activityDiagramParser = new ActivityDiagramParser();
        this.pathVisualizer = new PathVisualizer(new ColorSeeder());
        this.projectManager = new ProjectManager();

        this.testCaseTable = new InteractiveTable(new String[]{"Type", "Route Ids", "Count", "Show"}, getFreshTableData(), buildCellButtonClickHandler());
        this.scenariosTable = new InteractiveTable(new String[]{"Type", "Route Ids", "Show"}, getFreshScenariosTableData(), buildCellButtonClickHandler());
    }

    public void clearDiagramAnnotations() {
        ActivityDiagramEditor diagramEditor = this.projectManager.getCurrentDiagramEditor();
        IActivity activity = this.projectManager.getCurrentActivity();
        List<NodeRoute> routes = this.activityDiagramParser.getAllRoutes(activity);
        int[] routeIds = routes.stream().mapToInt(x -> x.id).toArray();

        pathVisualizer.toggleRouteByIds(activity, diagramEditor, activityDiagramParser, routes, routeIds, false);
    }

    private List<NodeRoute> getCurrentRoutes() {
        IActivity activity = this.projectManager.getCurrentActivity();
        return this.activityDiagramParser.getAllRoutes(activity);
    }

    public Container loadContent() {
        this.clearDiagramAnnotations();
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JScrollPane(this.testCaseTable.getTable()));
        panel.add(new JScrollPane(this.scenariosTable.getTable()));

        return panel;
    }

    private Object[][] getFreshTableData() {
        List<NodeRoute> routes = this.getCurrentRoutes();

        return this.nodeRouteDataTransformer.getAsObject(routes);
    }

    private Object[][] getFreshScenariosTableData() {
        List<NodeRoute> routes = this.activityDiagramParser.getScenarios(projectManager.getCurrentActivity());

        return this.scenariosNodeRouteDataTransformer.getAsObject(routes);
    }

    public void loadFreshTableData() {
        this.testCaseTable.setData(getFreshTableData());
        this.scenariosTable.setData(getFreshScenariosTableData());
    }

    private AbstractAction buildCellButtonClickHandler() {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                IActivity activity = projectManager.getCurrentActivity();
                List<NodeRoute> routes = getCurrentRoutes();

                JTable table = (JTable) e.getSource();

                boolean hasToShowRoutes = Boolean.parseBoolean(e.getActionCommand());

                String[] routeIdsAsStrings = table.getValueAt(table.getSelectedRow(), table.getColumn("Route Ids").getModelIndex()).toString().split(",");

                int[] routeIds = Stream.of(routeIdsAsStrings).mapToInt(Integer::parseInt).toArray();

                pathVisualizer.toggleRouteByIds(activity, projectManager.getCurrentDiagramEditor(), activityDiagramParser, routes, routeIds, hasToShowRoutes);
            }
        };
    }

}
