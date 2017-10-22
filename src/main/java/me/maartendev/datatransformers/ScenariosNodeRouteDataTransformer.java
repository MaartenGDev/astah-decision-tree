package me.maartendev.datatransformers;

import me.maartendev.nodes.NodeRoute;

import java.util.List;

public class ScenariosNodeRouteDataTransformer {
    public Object[][] getAsObject(List<NodeRoute> routes) {
        Object[][] data = new Object[routes.size()][3];

        int routeIndex = 0;
        for (NodeRoute route : routes) {
            data[routeIndex][0] = routeIndex == 0 ? "Happy path" : "Alternative path";
            data[routeIndex][1] = route.id;
            data[routeIndex][2] = "Show";
            routeIndex++;
        }

        return data;
    }
}
