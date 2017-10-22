package me.maartendev.views;


import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import javax.swing.*;
import java.awt.*;

public class PathFinderPanel extends JPanel implements IPluginExtraTabView, ProjectEventListener {

    private PathFinderView pathFinderView;

    public PathFinderPanel() {
        pathFinderView = new PathFinderView();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(createLabelPane(), BorderLayout.CENTER);
        addProjectEventListener();
    }

    private void addProjectEventListener() {
        try {
            ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
            projectAccessor.addProjectEventListener(this);
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }

    private Container createLabelPane() {
        return pathFinderView.loadContent();
    }

    @Override
    public void projectChanged(ProjectEvent e) {
        pathFinderView.loadFreshTableData();
    }

    @Override
    public void projectClosed(ProjectEvent e) {
    }

    @Override
    public void projectOpened(ProjectEvent e) {
    }

    @Override
    public void addSelectionListener(ISelectionListener listener) {
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public String getDescription() {
        return "Show Hello World here";
    }

    @Override
    public String getTitle() {
        return "Use cases";
    }

    public void activated() {
    }

    public void deactivated() {
    }
}