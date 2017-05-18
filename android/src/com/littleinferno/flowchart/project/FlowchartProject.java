package com.littleinferno.flowchart.project;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.littleinferno.flowchart.Files;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class FlowchartProject implements Parcelable {

    public static final String TAG = "FLOWCHART_PROJECT";
    private AndroidFunction currentScene;
    private Context context;
    private String name;
    private static AndroidPluginHandle pluginHandle;

    private FragmentManager fragmentManager;
    private AndroidVariableManager variableManager;
    private AndroidFunctionManager functionManager;

    public FlowchartProject(String name) {
        this.name = name;

        variableManager = new AndroidVariableManager(this);
        functionManager = new AndroidFunctionManager(this);
    }

    protected FlowchartProject(Parcel in) {
        currentScene = in.readParcelable(AndroidFunction.class.getClassLoader());
        name = in.readString();
        variableManager = in.readParcelable(AndroidVariableManager.class.getClassLoader());
        functionManager = in.readParcelable(AndroidFunctionManager.class.getClassLoader());
    }

    public static final Creator<FlowchartProject> CREATOR = new Creator<FlowchartProject>() {
        @Override
        public FlowchartProject createFromParcel(Parcel in) {
            return new FlowchartProject(in);
        }

        @Override
        public FlowchartProject[] newArray(int size) {
            return new FlowchartProject[size];
        }
    };

    public static FlowchartProject create(Context context, String name) {
        return new FlowchartProject(name);
    }

    public void setPlugin(AndroidPluginHandle plugin) {
        pluginHandle = plugin;
    }

    public void unloadPlugin() {
        pluginHandle.unload();
        pluginHandle = null;
    }

    public AndroidPluginHandle.NodeHandle getNodeHandle(final String nodeName) {
        return Stream.of(pluginHandle.getNodes())
                .filter(value -> value.getName().equals(nodeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find node: " + nodeName));
    }

    public List<AndroidPluginHandle.NodeHandle> getNodeHandles() {
        return pluginHandle.getNodes();
    }

    public AndroidPluginHandle.RuleHandle getRules() {
        return pluginHandle.getRules();
    }

    public AndroidFunction getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(AndroidFunction currentScene) {
        this.currentScene = currentScene;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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
                .create();

        String string = Environment.getExternalStorageDirectory().toString();
        String fn = string + "/flowchart_projects/saves/" + name + ".json";

        Observable.just(this)
                .observeOn(Schedulers.io())
                .map(flowchartProject -> getFunctionManager())
                .map(afm -> Stream.of(afm.getFunctions()).map(AndroidFunction::getSaveInfo).toArray(AndroidFunction.SimpleObject[]::new))
                .map(gson::toJson)
                .subscribe(s -> Files.writeToFile(fn, s));

    }

    public static FlowchartProject load(final Context context, String name) {
        FlowchartProject project = new FlowchartProject(name);

        String string = Environment.getExternalStorageDirectory().toString();

        try {
            project.setPlugin(new AndroidPluginHandle(Files.readToString(new File(string + "/flowchart_projects/plugins/new.js"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(string + "/flowchart_projects/saves/" + name + ".json");

        String save = Files.readToString(file);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();


        List<AndroidFunction.SimpleObject> list = gson.fromJson(save, getSavingType());

        Stream.of(list).forEach(project.functionManager::createFunction);

        return project;
    }

    public static Type getSavingType() {
        return new TypeToken<List<AndroidFunction.SimpleObject>>() {
        }.getType();
    }

    public void clearContext() {
        context = null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(currentScene, flags);
        dest.writeString(name);
        dest.writeParcelable(variableManager, flags);
        dest.writeParcelable(functionManager, flags);
    }
}
