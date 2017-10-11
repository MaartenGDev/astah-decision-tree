package me.maartendev;


import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.exception.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;


/**
 * Sample source codes for creating Astah model by Astah API.
 * Crate a package and two classes, then add an association between classes.
 * A class diagram is not generated in this sample.
 * To create a class diagram, please use [Auto caret class diagram] function in Astah.
 * Or, models in the structure tree in Astah can be dragged to a diagram.
 */

public class PathFinder implements IPluginActionDelegate {
    private ActivityTypeChecker typeChecker;

    public PathFinder() {
        typeChecker = new ActivityTypeChecker();
    }

    public Object run(IWindow window) {
        try {
            System.out.println("======== SEARCHING FOR ELEMENTS ========");

            ProjectAccessor project = AstahAPI.getAstahAPI().getProjectAccessor();

            ActivityDiagramEditor diagramEditor = project.getDiagramEditorFactory().getActivityDiagramEditor();
            diagramEditor.setDiagram(project.getProject().getDiagrams()[0]);

            IActivity activity = ((IActivityDiagram) diagramEditor.getDiagram()).getActivity();

            for (IActivityNode node : activity.getActivityNodes()) {
                if(typeChecker.isInitialNode(node)){
                    boolean initialNodeIsConnectedToDecisionNode = isConnectedToNodeOfType(node, typeChecker.getDecisionNodeType());
                    boolean initialNodeIsConnectedToEndNode = isConnectedToNodeOfType(node, typeChecker.getEndNodeType());

                    System.out.println("Is initial node connected to decision node: " + initialNodeIsConnectedToDecisionNode);
                    System.out.println("Is initial node connected to end node: " + initialNodeIsConnectedToEndNode);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isConnectedToNodeOfType(IActivityNode node, String type) throws InvalidUsingException {
        for (IFlow flow : node.getOutgoings()) {
            if (typeChecker.getTargetType(flow.getTarget()).equals(type)) {
                return true;
            } else if (flow.getTarget().getOutgoings().length > 0) {
                return isConnectedToNodeOfType(flow.getTarget(), type);
            }
        }

        return false;
    }
}

