package me.maartendev.projects;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.ActivityDiagramEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IActivity;
import com.change_vision.jude.api.inf.model.IActivityDiagram;
import com.change_vision.jude.api.inf.project.ProjectAccessor;

public class ProjectManager {
    private IActivity getActivityFromDiagram(ActivityDiagramEditor diagramEditor) {
        return ((IActivityDiagram) diagramEditor.getDiagram()).getActivity();

    }

    public ActivityDiagramEditor getCurrentDiagramEditor() {
        try {
            ProjectAccessor project = AstahAPI.getAstahAPI().getProjectAccessor();

            ActivityDiagramEditor diagramEditor = project.getDiagramEditorFactory().getActivityDiagramEditor();
            diagramEditor.setDiagram(project.getProject().getDiagrams()[0]);

            return diagramEditor;

        } catch (ClassNotFoundException | InvalidUsingException | InvalidEditingException | ProjectNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IActivity getCurrentActivity(){
        return this.getActivityFromDiagram(this.getCurrentDiagramEditor());
    }
}
