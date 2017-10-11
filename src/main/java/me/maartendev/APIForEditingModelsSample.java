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

public class APIForEditingModelsSample implements IPluginActionDelegate {

    public Object run(IWindow window) {
        try {
            System.out.println("======== SEARCHING FOR ELEMENTS ========");

            ProjectAccessor project = AstahAPI.getAstahAPI().getProjectAccessor();

            ActivityDiagramEditor test = project.getDiagramEditorFactory().getActivityDiagramEditor();

            test.setDiagram(project.getProject().getDiagrams()[0]);

            IActivityDiagram activityDiagram = (IActivityDiagram) test.getDiagram();

            IActivity activity = activityDiagram.getActivity();

            for (IActivityNode node : activity.getActivityNodes()) {
                System.out.println(node.getName() + " is connected with a decision node: " + (isConnectedToDecisionNode(node) ? "yes" : "no"));
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean isConnectedToDecisionNode(IActivityNode node) throws InvalidUsingException {
        for (IFlow flow : node.getOutgoings()) {
            if (isDecisionNode(flow.getTarget())) {
                return true;
            } else if (flow.getTarget().getOutgoings().length > 0) {
                return isConnectedToDecisionNode(flow.getTarget());
            }
        }

        return false;
    }

    private boolean isDecisionNode(IActivityNode node) throws InvalidUsingException {
        return node.getPresentations()[0].getType().equals("Decision Node & Merge Node");
    }

}

