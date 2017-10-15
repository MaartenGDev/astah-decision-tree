package me.maartendev;

import java.util.List;

public class NodeConnection {
    public ActivityNode source;
    public ActivityNode destination;

    public NodeConnection(ActivityNode source, ActivityNode end){
        this.source = source;
        this.destination = end;
    }
}
