package com.littleinferno.flowchart;

import android.support.constraint.ConstraintLayout;

import com.littleinferno.flowchart.plugin.PluginManager;

import java.io.File;

public class FlowchartProject {

    static FlowchartProject project;
    private File projectFolder;

    private Scene currentScene;
    private ConstraintLayout layout;

    public FlowchartProject() {

    }

    public static FlowchartProject getProject() {
        return project;
    }

    private PluginManager pluginManager;

    FlowchartProject(String name) {
        projectFolder = Files.newProjectFolder(name);
    }


    void loadNodePlugins(File file) {
        pluginManager.loadNodePlugins(file);
    }


    static FlowchartProject createNew(String name) {
        return project = new FlowchartProject(name);
    }

    public static FlowchartProject load() {
        return project = new FlowchartProject();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public ConstraintLayout getLayout() {
        return layout;
    }

    public void setLayout(ConstraintLayout layout) {
        this.layout = layout;
    }
}
