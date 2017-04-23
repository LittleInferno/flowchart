package com.littleinferno.flowchart.scene;

import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;

import java.util.ArrayList;
import java.util.List;

public class AndroidSceneManager implements ProjectModule {

    private List<AndroidSceneLayout> scenes;
    private FlowchartProject project;

    public AndroidSceneManager(FlowchartProject project, FlowchartProject flowchartProject) {
        this.project = project;
        scenes = new ArrayList<>();
    }

    @Override
    public FlowchartProject getProject() {
        return project;
    }
}
