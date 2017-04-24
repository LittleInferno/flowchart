package com.littleinferno.flowchart.project;

import android.os.Environment;
import android.support.constraint.ConstraintLayout;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Files;
import com.littleinferno.flowchart.plugin.AndroidPluginManager;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;

import java.io.File;

public class FlowchartProject {

    static FlowchartProject project;
    private File projectFolder;

    private AndroidSceneLayout currentScene;
    private ConstraintLayout layout;
    private AndroidPluginManager pluginManager;

    public FlowchartProject() {
        pluginManager = new AndroidPluginManager();

        String string = Environment.getExternalStorageDirectory().toString();


        pluginManager
                .loadNodePlugins(Stream.of(new File(string + "/flowchart_projects/plugins/nodes")
                        .listFiles())
                        .map(Files::readToString)
                        .toArray(String[]::new));

        pluginManager
                .loadCodeGeneratorPlugin(Files.readToString(new File(string + "/flowchart_projects/plugins/codegen.js")));
    }

    public static FlowchartProject getProject() {
        return project;
    }

    private FlowchartProject(String name) {
        projectFolder = Files.newProjectFolder(name);
    }

    public static FlowchartProject createNew(String name) {
        return project = new FlowchartProject(name);
    }

    public static FlowchartProject load() {
        return project = new FlowchartProject();
    }

    public AndroidSceneLayout getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(AndroidSceneLayout currentScene) {
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
