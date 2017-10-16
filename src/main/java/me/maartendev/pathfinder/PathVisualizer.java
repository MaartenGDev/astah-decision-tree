package me.maartendev.pathfinder;

import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import me.maartendev.nodes.NodeConnection;
import me.maartendev.nodes.NodeRoute;
import me.maartendev.seeders.ColorSeeder;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class PathVisualizer {
    private final int ACTIVITY_ID_INDICATOR_WIDTH = 20;
    private final int ACTIVITY_ID_INDICATOR_RADIUS = ACTIVITY_ID_INDICATOR_WIDTH / 2;

    private ColorSeeder colorSeeder;

    public PathVisualizer(ColorSeeder colorSeeder){
        this.colorSeeder = colorSeeder;
    }

    public void drawPathNumbers(ActivityDiagramEditor diagramEditor, List<NodeRoute> connections) throws InvalidEditingException {
        TransactionManager.beginTransaction();

        int connectionLabelOffset = ACTIVITY_ID_INDICATOR_RADIUS + 10;
        int connectionId = 1;

        for (NodeRoute connection : connections) {
            double connectionSourceY = connection.source.location.getY();

            double connectionDestinationY = connection.destination.location.getY();

            double connectionSourceX = connection.source.location.getX();
            double connectionDestinationX = connection.destination.location.getX();

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


            diagramEditor.createConnector(String.valueOf(connectionId), new Point2D.Double(xPosition, yPosition));

            connectionId++;
        }

        TransactionManager.endTransaction();
    }


    public void drawPathsOnRoute(NodeRoute route, Color color) {
        for (NodeConnection connection : route.route) {
            try {
                TransactionManager.beginTransaction();
                connection.line.setProperty("line.color", this.colorSeeder.getAsHexColor(color));
                TransactionManager.endTransaction();
            } catch (InvalidEditingException e) {
                e.printStackTrace();
            }
        }
    }

}
