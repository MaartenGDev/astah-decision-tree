package me.maartendev.nodes;

import com.change_vision.jude.api.inf.model.IActivityNode;
import com.change_vision.jude.api.inf.model.IFlow;
import me.maartendev.seeders.NumberSeeder;

import java.util.ArrayList;
import java.util.List;

public class ActivityNodeTreeBuilder {
    private NumberSeeder numberSeeder;

    public ActivityNodeTreeBuilder(NumberSeeder numberSeeder) {
        this.numberSeeder = numberSeeder;
    }

    public ActivityNodeTree build(IActivityNode activityNode) {
        return this.getTree(activityNode, new ActivityNodeTree());
    }

    private ActivityNodeTree getTree(IActivityNode activityNode, ActivityNodeTree rootTree) {
        rootTree.root = new ActivityNode(activityNode);
        rootTree.children.addAll(getChildren(activityNode));

        return rootTree;
    }

    private List<ActivityNodeTree> getChildren(IActivityNode node) {
        List<ActivityNodeTree> trees = new ArrayList<>();

        for (IFlow flow : node.getOutgoings()) {
            ActivityNodeTree tree = new ActivityNodeTree();

            tree.root = new ActivityNode(flow.getTarget());

            tree.children = getChildren(flow.getTarget());

            trees.add(tree);
        }

        return trees;
    }


    public List<NodeRoute> getAllRoutesToType(ActivityNode initialNode, ActivityNodeTree nodeTree, ActivityNodeTypes endType){
        this.numberSeeder.reset();

        return getAllRoutesToType(initialNode, nodeTree, endType, new NodeRoute(), new ArrayList<>());
    }

    private List<NodeRoute> getAllRoutesToType(ActivityNode initialNode, ActivityNodeTree nodeTree, ActivityNodeTypes endType, NodeRoute nodeRoute, List<NodeRoute> nodeRoutes) {
        nodeRoute.source = initialNode;

        for (ActivityNodeTree node : nodeTree.children) {
            nodeRoute.route.add(new NodeConnection(nodeTree.root, node.root, null));

            if (node.root.type == endType) {
                nodeRoute.id = this.numberSeeder.getNext();
                nodeRoute.destination = node.root;
                nodeRoutes.add(new NodeRoute(nodeRoute));
                nodeRoute.route = new ArrayList<>();
            }

            if (node.children.size() > 0) {
                nodeRoutes = getAllRoutesToType(initialNode, node, endType, nodeRoute, nodeRoutes);
            }
        }

        return nodeRoutes;
    }
}
