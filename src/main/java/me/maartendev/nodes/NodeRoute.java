package me.maartendev.nodes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NodeRoute {
    public int id;
    public ActivityNode source;
    public ActivityNode destination;
    public List<NodeConnection> route = new ArrayList<>();
    public Color activeLineColor;

    public NodeRoute(){}
    public NodeRoute(NodeRoute nodeRoute){
        this.id = nodeRoute.id;
        this.source = nodeRoute.source;
        this.destination = nodeRoute.destination;
        this.route = new ArrayList<>(nodeRoute.route);
        this.activeLineColor = nodeRoute.activeLineColor;
    }
    public NodeRoute(int id, ActivityNode source, ActivityNode destination, List<NodeConnection> route, Color activeLineColor) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.route = route;
        this.activeLineColor = activeLineColor;
    }

}
