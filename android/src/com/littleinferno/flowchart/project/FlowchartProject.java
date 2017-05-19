package com.littleinferno.flowchart.project;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.google.gson.reflect.TypeToken;
import com.littleinferno.flowchart.Files;
import com.littleinferno.flowchart.function.AndroidFunction;
import com.littleinferno.flowchart.function.AndroidFunctionManager;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;
import com.littleinferno.flowchart.variable.AndroidVariable;
import com.littleinferno.flowchart.variable.AndroidVariableManager;

import java.lang.reflect.Type;
import java.util.List;

public class FlowchartProject implements Parcelable {

    public static final String TAG = "FLOWCHART_PROJECT";
    public static final String PROJECT_NAME = "NAME";
    private AndroidFunction currentScene;
    private Context context;
    private String name;
    private static AndroidPluginHandle pluginHandle;

    private FragmentManager fragmentManager;
    private AndroidVariableManager variableManager;
    private AndroidFunctionManager functionManager;
    private String gen;

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
        gen = in.readString();
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

    public static FlowchartProject create(String name) {
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
        Files.saveProject(this);
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
        dest.writeString(gen);
    }

    public String getName() {
        return name;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public SimpleObject getSaveInfo() {

        List<AndroidFunction.SimpleObject> functions = Stream.of(functionManager.getFunctions())
                .map(AndroidFunction::getSaveInfo).toList();

        List<AndroidVariable.SimpleObject> variables = Stream.of(variableManager.getVariables())
                .map(AndroidVariable::getSaveInfo).toList();

        return new SimpleObject(pluginHandle.getPluginParams().getPluginName(), functions, variables);
    }

    public void init(SimpleObject s) throws Exception {

        if (s.plugin.equals("standart plugin")) {
            setPlugin(new AndroidPluginHandle(PluginHelper.getStandartPluginParams(context.getAssets()),
                    PluginHelper.getStandartPluginContent(context.getAssets())));
        } else {
            setPlugin(Files.loadPlugin(s.plugin));
        }
        Stream.of(s.functions).forEach(functionManager::createFunction);
        Stream.of(s.variables).forEach(variableManager::createVariable);
    }

    public static class SimpleObject {
        final String plugin;
        final List<AndroidFunction.SimpleObject> functions;
        final List<AndroidVariable.SimpleObject> variables;

        public SimpleObject(String plugin, List<AndroidFunction.SimpleObject> functions,
                            List<AndroidVariable.SimpleObject> variables) {
            this.plugin = plugin;
            this.functions = functions;
            this.variables = variables;
        }
    }
}
