package me.maartendev.datatransformers;

import me.maartendev.nodes.NodeRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeRouteDataTransformer {
    public Object[][] getAsObject(List<NodeRoute> routes) {
        Map<String, List<Integer>> routeTypes = new HashMap<>();

        for (NodeRoute route : routes) {
            String key = this.buildRouteTypeKey(route);

            if (routeTypes.containsKey(key)) {
                List<Integer> allIds = routeTypes.get(key);
                allIds.add(route.id);

                routeTypes.put(key, allIds);
            } else {
                List<Integer> routeIds = new ArrayList<>();
                routeIds.add(route.id);

                routeTypes.put(key, routeIds);
            }
        }

        Object[][] data = new Object[routeTypes.size()][4];

        int routeTypeIndex = 0;
        for (String routeTypeKey : routeTypes.keySet()) {
            data[routeTypeIndex][0] = routeTypeKey;
            data[routeTypeIndex][1] = String.join(",", convertIntegerToStringList(routeTypes.get(routeTypeKey)));
            data[routeTypeIndex][2] = routeTypes.get(routeTypeKey).size();
            data[routeTypeIndex][3] = "Toggle";
            routeTypeIndex++;
        }

        return data;
    }

    private String buildRouteTypeKey(NodeRoute route) {
        return route.source.type + " -> " + route.destination.type;
    }

    private List<String> convertIntegerToStringList(List<Integer> integers) {
        List<String> stringList = new ArrayList<>();

        for (Integer integer : integers) {
            stringList.add(integer.toString());
        }

        return stringList;
    }
}
