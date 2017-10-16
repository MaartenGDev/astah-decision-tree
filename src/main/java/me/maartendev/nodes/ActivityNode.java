package me.maartendev.nodes;

import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IActivityNode;
import com.change_vision.jude.api.inf.presentation.INodePresentation;

import java.awt.geom.Rectangle2D;

public class ActivityNode {

    public String id;
    public ActivityNodeTypes type;
    public Rectangle2D location;


    public ActivityNode(IActivityNode node) {
        ActivityNodeTypeConverter typeConverter = new ActivityNodeTypeConverter();

        id = node.getId();
        type = typeConverter.toEnum(node);
        location = getNodeLocation(node);
    }

    private Rectangle2D getNodeLocation(IActivityNode node) {
        try {
            return ((INodePresentation) node.getPresentations()[0]).getRectangle();
        } catch (InvalidUsingException e) {
            e.printStackTrace();
        }

        return null;
    }
}


