package me.maartendev.nodes;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IActivity;
import com.change_vision.jude.api.inf.model.IActivityNode;
import com.change_vision.jude.api.inf.model.IFlow;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;

import java.awt.geom.Rectangle2D;

public class ActivityNode {

    public String id;
    public ActivityNodeTypes type;
    public IPresentation presentation;
    public Rectangle2D location;
    public String text;
    public IPresentation line;


    public ActivityNode(IActivityNode node) {
        ActivityNodeTypeConverter typeConverter = new ActivityNodeTypeConverter();

        id = node.getId();
        type = typeConverter.toEnum(node);
        location = getNodeLocation(node);
        presentation = getPresentation(node);
        text = node.getName();
    }

    public void setLine(IFlow flow){
        try {
            line = flow.getPresentations()[0];
        } catch (InvalidUsingException e) {
            e.printStackTrace();
        }
    }

    private Rectangle2D getNodeLocation(IActivityNode node) {
        try {
            return ((INodePresentation) node.getPresentations()[0]).getRectangle();
        } catch (InvalidUsingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private IPresentation getPresentation(IActivityNode node){
        try {
            return node.getPresentations()[0];
        } catch (InvalidUsingException e) {
            e.printStackTrace();
        }

        return null;
    }
}


