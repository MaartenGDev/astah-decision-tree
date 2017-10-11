package me.maartendev;


import javax.swing.*;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;
import com.change_vision.jude.api.inf.ui.IWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PathFinderView extends JPanel implements IPluginExtraTabView, ProjectEventListener {

	public PathFinderView() {
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
		JLabel label = new JLabel("hello world");
		JScrollPane pane = new JScrollPane(label);
		return pane;
	}

	@Override
	public void projectChanged(ProjectEvent e) {
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
		return "Hello World View";
	}

	public void activated() {

	}

	public void deactivated() {

	}
}