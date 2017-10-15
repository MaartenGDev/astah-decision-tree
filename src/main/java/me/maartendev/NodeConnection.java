package me.maartendev;

import com.change_vision.jude.api.inf.presentation.IPresentation;

import java.util.List;

public class NodeConnection {
    public ActivityNode source;
    public ActivityNode destination;
    public IPresentation line;

    public NodeConnection(ActivityNode source, ActivityNode end, IPresentation line){
        this.source = source;
        this.destination = end;
        this.line = line;
    }
}
