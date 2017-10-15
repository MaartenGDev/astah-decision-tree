package me.maartendev;

import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.presentation.IPresentation;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class PathVisualizer {
    private final int ACTIVITY_ID_INDICATOR_WIDTH = 20;
    private final int ACTIVITY_ID_INDICATOR_RADIUS = ACTIVITY_ID_INDICATOR_WIDTH / 2;

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

    private Color getColor(int index) {
        return new Color[]{
                Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.PINK, Color.MAGENTA
        }[index];
    }

    private String getAsHexColor(Color color){
        return "#"+Integer.toHexString(color.getRGB()).substring(2);
    }

    public void drawPathsForRoutes(List<NodeRoute> routes){
        int routeId = 0;

        for(NodeRoute route : routes){
            drawPathsOnRoute(route, routeId);
            routeId++;
        }
    }

    private void drawPathsOnRoute(NodeRoute route, int routeIndex) {
        for (NodeConnection connection : route.route) {
            try {
                TransactionManager.beginTransaction();
                connection.line.setProperty("line.color", getAsHexColor(getColor(routeIndex)));
                TransactionManager.endTransaction();
            } catch (InvalidEditingException e) {
                e.printStackTrace();
            }
        }
    }

}
