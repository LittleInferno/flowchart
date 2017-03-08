package com.littleinferno.flowchart.util;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.littleinferno.flowchart.project.Project;


public class ProjectStage extends Stage {

    private final Project project;

    public ProjectStage(Viewport viewport, Project project) {
        super(viewport);
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
