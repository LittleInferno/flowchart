package com.littleinferno.flowchart.project;

import android.app.FragmentManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.View;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.littleinferno.flowchart.Files;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.plugin.AndroidPluginManager;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

public class FlowchartProject {

    private static FlowchartProject project;

    private AndroidFunction currentScene;
    private View layout;
    private AndroidPluginManager pluginManager;
    private Context context;
    private String name;

    private FragmentManager fragmentManager;
    private AndroidVariableManager variableManager;

    private AndroidFunctionManager functionManager;

    public FlowchartProject(Context context, String name) {
        this.context = context;
        this.name = name;

        pluginManager = new AndroidPluginManager();
        variableManager = new AndroidVariableManager(this);
        functionManager = new AndroidFunctionManager(this);
        String string = Environment.getExternalStorageDirectory().toString();

        pluginManager.loadPlugin(Files.readToString(new File(string + "/flowchart_projects/plugins/new.js")));
    }

    public static FlowchartProject getProject() {
        return project;
    }

    private FlowchartProject(String name) {
//        projectFolder = Files.newProjectFolder(name);
    }

    public static FlowchartProject createNew(String name) {
        return project = new FlowchartProject(name);
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

    public AndroidVariableManager getVariableManager() {
        return variableManager;
    }

    public AndroidFunctionManager getFunctionManager() {
        return functionManager;
    }


    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AndroidFunction.class, new AndroidFunction.Serializer())
                .create();

        String s = gson.toJson(functionManager.getFunctions());

        String string = Environment.getExternalStorageDirectory().toString();
        File file = new File(string + "/flowchart_projects/saves/" + name + ".json");
        try {
            file.createNewFile();
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.print(s);
            writer.close();

            MediaScannerConnection.scanFile(getContext(), new String[]{file.getPath()}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
            // do something
        }
    }

    public static FlowchartProject load(final Context context, String name) {
        project = new FlowchartProject(context, name);

        String string = Environment.getExternalStorageDirectory().toString();
        File file = new File(string + "/flowchart_projects/saves/" + name + ".json");

        String save = Files.readToString(file);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AndroidFunction.class, new AndroidFunction.Serializer())
                .create();

        Type type = new TypeToken<List<AndroidFunction.SimpleObject>>() {
        }.getType();

        //noinspection unchecked
        List<AndroidFunction.SimpleObject> list = gson.fromJson(save, type);

        Stream.of(list).forEach(project.functionManager::createFunction);

        return project;
    }
}
