package me.maartendev;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IActivityNode;

public class ActivityTypeChecker {
    public boolean isInitialNode(IActivityNode node) throws InvalidUsingException {
        return getTargetType(node).equals("InitialNode");
    }

    public String getTargetType(IActivityNode node) throws InvalidUsingException {
        return node.getPresentations()[0].getType();
    }

    public String getDecisionNodeType(){
        return "Decision Node & Merge Node";
    }

    public String getEndNodeType(){
        return "ActivityFinal";
    }
}
