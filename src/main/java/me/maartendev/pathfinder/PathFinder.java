package me.maartendev.pathfinder;


import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import me.maartendev.nodes.*;
import me.maartendev.seeders.ColorSeeder;
import me.maartendev.seeders.NumberSeeder;

import java.util.ArrayList;
import java.util.List;


public class PathFinder implements IPluginActionDelegate {
    private ActivityNodeTypeConverter typeConverter;
    private PathVisualizer pathVisualizer;
    private ColorSeeder colorSeeder;
    private NumberSeeder numberSeeder;

    public PathFinder() {
        typeConverter = new ActivityNodeTypeConverter();
        colorSeeder = new ColorSeeder();
        numberSeeder = new NumberSeeder();
        pathVisualizer = new PathVisualizer(colorSeeder);
    }

    public Object run(IWindow window) {


        return null;
    }

    public List<NodeRoute> getAllRoutes(IActivity activity) {
        List<NodeRoute> allConnections = new ArrayList<>();

        try {
            for (IActivityNode node : activity.getActivityNodes()) {
                ActivityNodeTypes nodeType = typeConverter.toEnum(node);

                if (nodeType == ActivityNodeTypes.INITIAL_NODE) {
                    allConnections.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.DECISION_NODE, new ArrayList<>(), new ArrayList<>(), true));
                    allConnections.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.FINAL_NODE, new ArrayList<>(), new ArrayList<>(), true));

                } else if (nodeType == ActivityNodeTypes.DECISION_NODE) {
                    allConnections.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.DECISION_NODE, new ArrayList<>(), new ArrayList<>(), true));
                    allConnections.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.FINAL_NODE, new ArrayList<>(), new ArrayList<>(), true));
                }
            }
        } catch (InvalidUsingException | InvalidEditingException e) {
            e.printStackTrace();
        }

        return allConnections;
    }

    private List<NodeRoute> getDirectlyConnectedToNodeOfTypeCount(IActivityNode node, ActivityNodeTypes typeToFind, List<NodeConnection> route, List<NodeRoute> nodeRoutes, boolean isRootNode) throws InvalidUsingException, InvalidEditingException {
        for (IFlow flow : node.getOutgoings()) {
            IActivityNode target = flow.getTarget();
            ActivityNodeTypes targetType = typeConverter.toEnum(target);

            if (isRootNode) {
                route = new ArrayList<>();
            }

            route.add(new NodeConnection(new ActivityNode(node), new ActivityNode(target), flow.getPresentations()[0]));

            if (targetType == typeToFind) {
                nodeRoutes.add(new NodeRoute(this.numberSeeder.getNext(), route.get(0).source, route.get(route.size() - 1).destination, route, this.colorSeeder.getNextColor()));
            } else if (typeToFind != ActivityNodeTypes.DECISION_NODE && targetType == ActivityNodeTypes.DECISION_NODE) {
                return nodeRoutes;
            } else if (target.getOutgoings().length > 0) {
                nodeRoutes = getDirectlyConnectedToNodeOfTypeCount(target, typeToFind, route, nodeRoutes, false);
            }
        }

        return nodeRoutes;
    }

    private List<NodeConnection> getPathBetweenNodes(NodeConnection start, NodeConnection end, List<NodeConnection> initialConnections, List<NodeConnection> connections) {

        NodeConnection targetConnection = initialConnections.stream().filter(x -> x.source.id.equals(start.source.id)).findFirst().get();

        if (connections.size() == 0) {
            connections.add(start);
        }

        connections.add(targetConnection);

        if (targetConnection.destination.type == end.destination.type) {
            return connections;
        }


        return getPathBetweenNodes(targetConnection, end, initialConnections, connections);
    }
}

