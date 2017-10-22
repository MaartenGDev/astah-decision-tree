package me.maartendev.nodes;

import java.util.ArrayList;
import java.util.List;

public class ActivityNodeTree {
    public ActivityNode root;
    public List<ActivityNodeTree> children = new ArrayList<>();

    public ActivityNodeTree(){}

    public ActivityNodeTree(ActivityNodeTree copy){
        this.root = copy.root;
        this.children = new ArrayList<>(copy.children);
    }
}
