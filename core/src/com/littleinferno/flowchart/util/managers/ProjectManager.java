package com.littleinferno.flowchart.util.managers;

import com.littleinferno.flowchart.project.Project;

public abstract class ProjectManager extends BaseManager {

    private Project project;

    public ProjectManager(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
