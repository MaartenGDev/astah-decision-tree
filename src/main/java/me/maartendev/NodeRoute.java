package me.maartendev;

import java.util.List;

public class NodeRoute {
    public ActivityNode source;
    public ActivityNode destination;
    public List<NodeConnection> route;

    public NodeRoute(ActivityNode source, ActivityNode destination, List<NodeConnection> route){
        this.source = source;
        this.destination = destination;
        this.route = route;
    }

}
