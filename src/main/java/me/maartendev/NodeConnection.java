package me.maartendev;

public class NodeConnection {
    public ActivityNode source;
    public ActivityNode destination;

    public NodeConnection(ActivityNode source, ActivityNode end){
        this.source = source;
        this.destination = end;
    }
}
