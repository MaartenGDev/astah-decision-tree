package me.maartendev.nodes;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IActivityNode;

public class ActivityNodeTypeConverter {

    public ActivityNodeTypes toEnum(IActivityNode node) {
        String type = "";

        try {
            type = node.getPresentations()[0].getType();
        } catch (InvalidUsingException e) {
            e.printStackTrace();
        }

        switch (type) {
            case "InitialNode":
                return ActivityNodeTypes.INITIAL_NODE;
            case "Decision Node & Merge Node":
                boolean hasMultipleOutputs = node.getOutgoings().length > 1;
                return hasMultipleOutputs ? ActivityNodeTypes.DECISION_NODE : ActivityNodeTypes.MERGE_NODE;
            case "ActivityFinal":
                return ActivityNodeTypes.FINAL_NODE;
            case "Connector":
                return ActivityNodeTypes.CONNECTOR;
            case "Action":
                return ActivityNodeTypes.ACTION_NODE;
            default:
                return ActivityNodeTypes.ACTION_NODE;
        }
    }
}
