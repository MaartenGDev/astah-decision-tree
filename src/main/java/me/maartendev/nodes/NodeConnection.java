package me.maartendev.nodes;

import com.change_vision.jude.api.inf.presentation.IPresentation;

public class NodeConnection {
    public ActivityNode source;
    public ActivityNode destination;
    public IPresentation line;

    public NodeConnection(ActivityNode source, ActivityNode end, IPresentation line) {
        this.source = source;
        this.destination = end;
        this.line = line;
    }
}
