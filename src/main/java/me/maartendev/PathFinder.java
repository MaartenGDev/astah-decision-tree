package me.maartendev;


import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.editor.DiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PathFinder implements IPluginActionDelegate {
    private ActivityNodeTypeConverter typeConverter;
    private PathVisualizer pathVisualizer;

    public PathFinder() {
        typeConverter = new ActivityNodeTypeConverter();
        pathVisualizer = new PathVisualizer();
    }

    public Object run(IWindow window) {
        try {
            System.out.println("======== SEARCHING FOR ELEMENTS ========");

            ProjectAccessor project = AstahAPI.getAstahAPI().getProjectAccessor();

            ActivityDiagramEditor diagramEditor = project.getDiagramEditorFactory().getActivityDiagramEditor();
            diagramEditor.setDiagram(project.getProject().getDiagrams()[0]);

            IActivity activity = ((IActivityDiagram) diagramEditor.getDiagram()).getActivity();

            List<NodeRoute> initialToDecisionNodes = new ArrayList<>();
            List<NodeRoute> decisionToDecisionNodes = new ArrayList<>();
            List<NodeRoute> decisionToFinalNodes = new ArrayList<>();
            List<NodeRoute> initialToFinalNodes = new ArrayList<>();

            for (IActivityNode node : activity.getActivityNodes()) {
                ActivityNodeTypes nodeType = typeConverter.toEnum(node);

                if (nodeType == ActivityNodeTypes.INITIAL_NODE) {
                    initialToDecisionNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.DECISION_NODE, new ArrayList<>(), new ArrayList<>(), true));
                    initialToFinalNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.FINAL_NODE, new ArrayList<>(), new ArrayList<>(), true));

                } else if (nodeType == ActivityNodeTypes.DECISION_NODE) {
                    decisionToDecisionNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.DECISION_NODE, new ArrayList<>(), new ArrayList<>(), true));
                    decisionToFinalNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, ActivityNodeTypes.FINAL_NODE, new ArrayList<>(), new ArrayList<>(), true));
                }
            }


            System.out.println("Initial -> Decision node: " + initialToDecisionNodes.size());
            System.out.println("Decision -> Decision node: " + decisionToDecisionNodes.size());
            System.out.println("Decision -> Activity final node " + decisionToFinalNodes.size());
            System.out.println("Initial -> Activity final node: " + initialToFinalNodes.size());

            System.out.println("Total paths: " + (initialToDecisionNodes.size() + decisionToDecisionNodes.size() + decisionToFinalNodes.size() + initialToFinalNodes.size()));


            List<NodeRoute> allConnections = new ArrayList<>();
            allConnections.addAll(initialToDecisionNodes);
            allConnections.addAll(decisionToDecisionNodes);
            allConnections.addAll(decisionToFinalNodes);
            allConnections.addAll(initialToFinalNodes);

            NodeRoute initialNodeConnection = allConnections.stream().filter(x -> x.source.type == ActivityNodeTypes.INITIAL_NODE).findFirst().orElseGet(null);
            NodeRoute finalNodeConnection = allConnections.stream().filter(x -> x.destination.type == ActivityNodeTypes.FINAL_NODE).findFirst().orElseGet(null);

            // List<NodeConnection> myPath = getPathBetweenNodes(initialNodeConnection, finalNodeConnection, allConnections, new ArrayList<>());

            for (NodeRoute connection : allConnections) {
                System.out.println(connection.source.type + " -> " + connection.destination.type);
            }

            pathVisualizer.drawPathNumbers(diagramEditor, allConnections);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<NodeRoute> getDirectlyConnectedToNodeOfTypeCount(IActivityNode node, ActivityNodeTypes typeToFind, List<NodeConnection> route, List<NodeRoute> nodeRoutes, boolean isRootNode) throws InvalidUsingException {
        for (IFlow flow : node.getOutgoings()) {
            IActivityNode target = flow.getTarget();
            ActivityNodeTypes targetType = typeConverter.toEnum(target);

            if(isRootNode){
                route = new ArrayList<>();
            }

            route.add(new NodeConnection(new ActivityNode(node), new ActivityNode(target)));

            if (targetType == typeToFind) {
                nodeRoutes.add(new NodeRoute(route.get(0).source, route.get(route.size() - 1).destination, route));
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

