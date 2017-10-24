package me.maartendev.nodes;

import com.change_vision.jude.api.inf.model.IActivityNode;
import com.change_vision.jude.api.inf.model.IFlow;
import me.maartendev.seeders.ColorSeeder;
import me.maartendev.seeders.NumberSeeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityNodeTreeBuilder {
    private NumberSeeder numberSeeder;
    private ColorSeeder colorSeeder;

    public ActivityNodeTreeBuilder(NumberSeeder numberSeeder, ColorSeeder colorSeeder) {
        this.numberSeeder = numberSeeder;
        this.colorSeeder = colorSeeder;
    }

    public ActivityNodeTree build(IActivityNode activityNode) {
        return this.getTree(activityNode, new ActivityNodeTree());
    }

    private ActivityNodeTree getTree(IActivityNode activityNode, ActivityNodeTree rootTree) {
        rootTree.root = new ActivityNode(activityNode);
        rootTree.children.addAll(getChildren(new ActivityNode(activityNode), activityNode.getOutgoings(), new ArrayList<>()));

        return rootTree;
    }

    private List<ActivityNodeTree> getChildren(ActivityNode node, IFlow[] possibleDirections, List<NodeConnection> visitedConnections) {
        List<ActivityNodeTree> trees = new ArrayList<>();

        for (IFlow flow : possibleDirections) {
            IActivityNode target = flow.getTarget();
            ActivityNode rootNode = new ActivityNode(target);

            NodeConnection myConn = new NodeConnection(node, rootNode, null);

            ActivityNodeTree tree = new ActivityNodeTree();

            visitedConnections.add(myConn);

            tree.root = rootNode;
            tree.root.setLine(flow);

            IFlow[] possibleFlows = convertToArray(Arrays.stream(target.getOutgoings()).filter(x -> rootNode.type != ActivityNodeTypes.DECISION_NODE || !hasConnectedToSelf(rootNode, visitedConnections, new NodeConnection(rootNode, new ActivityNode(x.getTarget()), null), true)).collect(Collectors.toList()));

            tree.children = getChildren(rootNode, possibleFlows, visitedConnections);

            trees.add(tree);
        }

        return trees;
    }

    private IFlow[] convertToArray(List<IFlow> list) {
        IFlow[] array = new IFlow[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    private boolean hasConnectedToSelf(ActivityNode node, List<NodeConnection> visitedConnections, NodeConnection connection, boolean searchFromRoot) {
        List<NodeConnection> receivedConnection = this.getConnectedNode(visitedConnections, connection, searchFromRoot);

        if (receivedConnection.size() == 0) {
            return false;
        }

        List<NodeConnection> pathsNotVisited = new ArrayList<>(visitedConnections);

        for (NodeConnection option : receivedConnection) {
            if (option.destination.id.equals(node.id)) {
                return true;
            }
        }

        return receivedConnection.stream().anyMatch(x -> hasConnectedToSelf(node, pathsNotVisited, x, false));
    }

    private List<NodeConnection> getConnectedNode(List<NodeConnection> connections, NodeConnection destination, boolean searchFromRoot) {
        return connections.stream().filter(x -> searchFromRoot ? (x.source.id.equals(destination.source.id) && (x.destination.id.equals(destination.destination.id))) : x.source.id.equals(destination.destination.id)).collect(Collectors.toList());
    }


    public List<NodeRoute> getAllRoutesToType(ActivityNode initialNode, ActivityNodeTree nodeTree, ActivityNodeTypes endType) {
        this.numberSeeder.reset();

        return getAllRoutesToType(initialNode, nodeTree, endType, new NodeRoute(), new ArrayList<>(), new ArrayList<>());
    }

    private List<NodeRoute> getAllRoutesToType(ActivityNode initialNode, ActivityNodeTree nodeTree, ActivityNodeTypes endType, NodeRoute nodeRoute, List<NodeRoute> nodeRoutes, List<NodeConnection> routeBeforeSwitch) {
        nodeRoute.source = initialNode;

        if (nodeTree.children.size() > 1) {
            routeBeforeSwitch = new ArrayList<>(nodeRoute.route);
        }

        for (ActivityNodeTree node : nodeTree.children) {
            nodeRoute.route.add(new NodeConnection(nodeTree.root, node.root, node.root.line));

            if (node.root.type == endType) {
                nodeRoute.id = this.numberSeeder.getNext();
                nodeRoute.activeLineColor = this.colorSeeder.getNext();

                nodeRoute.destination = node.root;
                nodeRoutes.add(new NodeRoute(nodeRoute));
                nodeRoute.route = new ArrayList<>(routeBeforeSwitch);
            }

            if (node.children.size() > 0) {
                nodeRoutes = getAllRoutesToType(initialNode, node, endType, nodeRoute, nodeRoutes, routeBeforeSwitch);
            }
        }

        return nodeRoutes;
    }
}
