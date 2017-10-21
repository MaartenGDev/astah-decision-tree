package me.maartendev.pathfinder;

import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IActivity;
import me.maartendev.nodes.ActivityNodeTypes;
import me.maartendev.nodes.NodeConnection;
import me.maartendev.nodes.NodeRoute;
import me.maartendev.seeders.ColorSeeder;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PathVisualizer {
    private final int ACTIVITY_ID_INDICATOR_WIDTH = 20;
    private final int ACTIVITY_ID_INDICATOR_RADIUS = ACTIVITY_ID_INDICATOR_WIDTH / 2;

    private ColorSeeder colorSeeder;

    public PathVisualizer(ColorSeeder colorSeeder) {
        this.colorSeeder = colorSeeder;
    }

    public void drawPathNumbers(ActivityDiagramEditor diagramEditor, List<NodeRoute> connections) {
        int connectionLabelOffset = ACTIVITY_ID_INDICATOR_RADIUS + 10;

        for (NodeRoute route : connections) {
            double connectionSourceY = route.source.location.getY();

            double connectionDestinationY = route.destination.location.getY();

            double connectionSourceX = route.source.location.getX();
            double connectionDestinationX = route.destination.location.getX();

            double yClosestToTop = Math.min(connectionSourceY, connectionDestinationY);
            double yClosestToBottom = Math.max(connectionSourceY, connectionDestinationY);

            double xClosestToLeft = Math.min(connectionSourceX, connectionDestinationX);
            double xClosestToRight = Math.max(connectionSourceX, connectionDestinationX);

            double yDifference = yClosestToBottom - yClosestToTop;
            double yPosition = yClosestToTop + (yDifference / 2);

            double xDifference = xClosestToRight - xClosestToLeft;
            double xPosition = xClosestToLeft + (xDifference / 2);

            boolean isHorizontalLine = connectionSourceY == connectionDestinationY;

            if (isHorizontalLine) {
                yPosition -= connectionLabelOffset;
            } else {
                xPosition += connectionLabelOffset;
            }

            try {
                TransactionManager.beginTransaction();

                diagramEditor.createConnector(String.valueOf(route.id), new Point2D.Double(xPosition, yPosition));
                TransactionManager.endTransaction();
            } catch (InvalidEditingException e) {
                TransactionManager.abortTransaction();
                e.printStackTrace();
            }
        }

    }


    public void drawPathsOnRoute(NodeRoute route, Color color) {
        for (NodeConnection connection : route.route) {
            try {
                TransactionManager.beginTransaction();
                connection.line.setProperty("line.color", this.colorSeeder.getAsHexColor(color));
                TransactionManager.endTransaction();
            } catch (InvalidEditingException e) {
                TransactionManager.abortTransaction();
                e.printStackTrace();
            }
        }
    }

    private NodeRoute getRouteById(List<NodeRoute> routes, Integer id) {
        return routes.stream().filter(x -> x.id == id).findFirst().orElse(null);
    }

    public void toggleRouteByIds(IActivity activity, ActivityDiagramEditor diagramEditor, ActivityDiagramParser diagramParser, List<NodeRoute> routes, int[] ids, boolean isActive) {
        List<NodeRoute> activeRoutes = new ArrayList<>();


        for (Integer routeId : ids) {
            NodeRoute route = getRouteById(routes, routeId);

            if (isActive) {
                activeRoutes.add(route);
            } else {
                diagramParser.deleteWhere(activity, diagramEditor, ActivityNodeTypes.CONNECTOR, String.valueOf(routeId));
            }

            this.drawPathsOnRoute(route, isActive ? route.activeLineColor : Color.BLACK);
        }

        this.drawPathNumbers(diagramEditor, activeRoutes);
    }

}
