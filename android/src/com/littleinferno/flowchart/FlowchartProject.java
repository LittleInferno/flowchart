package com.littleinferno.flowchart;

import android.os.Environment;
import android.support.constraint.ConstraintLayout;

import com.littleinferno.flowchart.plugin.AndroidPluginManager;

import java.io.File;

public class FlowchartProject {

    static FlowchartProject project;
    private File projectFolder;

    private Scene currentScene;
    private ConstraintLayout layout;
    private AndroidPluginManager pluginManager;

    public FlowchartProject() {
        pluginManager = new AndroidPluginManager();

        String string = Environment.getExternalStorageDirectory().toString();
        pluginManager.loadNodePlugins(new File(string + "/flowchart_projects/plugins/"));
    }

    public static FlowchartProject getProject() {
        return project;
    }

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

    public AndroidPluginManager getPluginManager() {
        return pluginManager;
    }
}
