package com.littleinferno.flowchart.project;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Environment;
import android.view.View;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Files;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.plugin.AndroidPluginManager;

import java.io.File;

public class FlowchartProject {

    static FlowchartProject project;
    private File projectFolder;

    private AndroidFunction currentScene;
    private View layout;
    private AndroidPluginManager pluginManager;
    private Context context;

    private FragmentManager fragmentManager;

    public FlowchartProject(Context context) {
        this.context = context;

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

    public static FlowchartProject load(final Context context) {
        return project = new FlowchartProject(context);
    }

    public AndroidFunction getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(AndroidFunction currentScene) {
        this.currentScene = currentScene;
    }

    public View getLayout() {
        return layout;
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }

    public AndroidPluginManager getPluginManager() {
        return pluginManager;
    }

    public Context getContext() {
        return context;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
