package com.littleinferno.flowchart.util;

import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.managers.BaseManager;

public abstract class ProjectManager extends BaseManager {

    private Project project;

    public ProjectManager(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
