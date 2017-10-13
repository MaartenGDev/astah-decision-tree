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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


/**
 * Sample source codes for creating Astah model by Astah API.
 * Crate a package and two classes, then add an association between classes.
 * A class diagram is not generated in this sample.
 * To create a class diagram, please use [Auto caret class diagram] function in Astah.
 * Or, models in the structure tree in Astah can be dragged to a diagram.
 */

public class PathFinder implements IPluginActionDelegate {
    private ActivityNodeTypeConverter typeConverter;

    public PathFinder() {
        typeConverter = new ActivityNodeTypeConverter();
    }

    public Object run(IWindow window) {
        try {
            System.out.println("======== SEARCHING FOR ELEMENTS ========");

            ProjectAccessor project = AstahAPI.getAstahAPI().getProjectAccessor();

            ActivityDiagramEditor diagramEditor = project.getDiagramEditorFactory().getActivityDiagramEditor();
            diagramEditor.setDiagram(project.getProject().getDiagrams()[0]);

            IActivity activity = ((IActivityDiagram) diagramEditor.getDiagram()).getActivity();

            List<NodeConnection> initialToDecisionNodes = new ArrayList<>();
            List<NodeConnection> decisionToDecisionNodes = new ArrayList<>();
            List<NodeConnection> decisionToFinalNodes = new ArrayList<>();
            List<NodeConnection> initialToFinalNodes = new ArrayList<>();

            for (IActivityNode node : activity.getActivityNodes()) {
                ActivityNodeTypes nodeType = typeConverter.toEnum(node);

                if (nodeType == ActivityNodeTypes.INITIAL_NODE) {
                    initialToDecisionNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, node, ActivityNodeTypes.DECISION_NODE, new ArrayList<>()));
                    initialToFinalNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, node, ActivityNodeTypes.FINAL_NODE, new ArrayList<>()));

                } else if (nodeType == ActivityNodeTypes.DECISION_NODE) {
                    decisionToDecisionNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, node, ActivityNodeTypes.DECISION_NODE, new ArrayList<>()));
                    decisionToFinalNodes.addAll(getDirectlyConnectedToNodeOfTypeCount(node, node, ActivityNodeTypes.FINAL_NODE, new ArrayList<>()));
                }
            }


            System.out.println("Initial -> Decision node: " + initialToDecisionNodes.size());
            System.out.println("Decision -> Decision node: " + decisionToDecisionNodes.size());
            System.out.println("Decision -> Activity final node " + decisionToFinalNodes.size());
            System.out.println("Initial -> Activity final node: " + initialToFinalNodes.size());

            System.out.println("Total paths: " + (initialToDecisionNodes.size() + decisionToDecisionNodes.size() + decisionToFinalNodes.size() + initialToFinalNodes.size()));


            List<NodeConnection> allConnections = new ArrayList<>();
            allConnections.addAll(initialToDecisionNodes);
            allConnections.addAll(decisionToDecisionNodes);
            allConnections.addAll(decisionToFinalNodes);
            allConnections.addAll(initialToFinalNodes);

            NodeConnection initialNodeConnection = allConnections.stream().filter(x -> x.source.type == ActivityNodeTypes.INITIAL_NODE).findFirst().orElseGet(null);
            NodeConnection finalNodeConnection = allConnections.stream().filter(x -> x.destination.type == ActivityNodeTypes.FINAL_NODE).findFirst().orElseGet(null);

            // List<NodeConnection> myPath = getPathBetweenNodes(initialNodeConnection, finalNodeConnection, allConnections, new ArrayList<>());

            for (NodeConnection connection : allConnections) {
                System.out.println(connection.source.type + " -> " + connection.destination.type);
            }

            drawPathNumbers(diagramEditor, allConnections);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<NodeConnection> getDirectlyConnectedToNodeOfTypeCount(IActivityNode initialNode, IActivityNode node, ActivityNodeTypes typeToFind, List<NodeConnection> connections) throws InvalidUsingException {
        for (IFlow flow : node.getOutgoings()) {
            IActivityNode target = flow.getTarget();
            ActivityNodeTypes targetType = typeConverter.toEnum(target);

            if (targetType == typeToFind) {
                NodeConnection connection = new NodeConnection(new ActivityNode(initialNode), new ActivityNode(target));

                connections.add(connection);
            } else if (typeToFind != ActivityNodeTypes.DECISION_NODE && targetType == ActivityNodeTypes.DECISION_NODE) {
                return connections;
            } else if (target.getOutgoings().length > 0) {
                connections = getDirectlyConnectedToNodeOfTypeCount(initialNode, target, typeToFind, connections);
            }
        }

        return connections;
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

    private void drawPathNumbers(ActivityDiagramEditor diagramEditor, List<NodeConnection> connections) throws InvalidEditingException {
        TransactionManager.beginTransaction();

        int connectionId = 1;

        for (NodeConnection connection : connections) {
            Rectangle2D sourceNodeLocation = connection.source.location;
            Rectangle2D destinationNodeLocation = connection.destination.location;

            diagramEditor.createConnector(String.valueOf(connectionId), new Point2D.Double( sourceNodeLocation.getX(),  destinationNodeLocation.getY() - sourceNodeLocation.getY()));

            connectionId++;
        }

        TransactionManager.endTransaction();
    }
}

