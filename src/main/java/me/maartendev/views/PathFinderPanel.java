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

    public Container content;

    public PathFinderPanel() {
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
        try {
            content = (new PathFinderView()).getContent();
            return content;
        } catch (InvalidUsingException | InvalidEditingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void projectChanged(ProjectEvent e) {
        this.revalidate();
        this.remove(content);
        try {
            this.add((new PathFinderView()).getContent());
        } catch (InvalidUsingException e1) {
            e1.printStackTrace();
        } catch (InvalidEditingException e1) {
            e1.printStackTrace();
        }
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
        this.repaint();
        this.revalidate();
    }

    public void deactivated() {
        this.repaint();
    }
}