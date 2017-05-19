package com.littleinferno.flowchart.project;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.google.gson.reflect.TypeToken;
import com.littleinferno.flowchart.util.Files;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.function.FunctionManager;
import com.littleinferno.flowchart.plugin.AndroidPluginHandle;
import com.littleinferno.flowchart.plugin.PluginHelper;
import com.littleinferno.flowchart.variable.Variable;
import com.littleinferno.flowchart.variable.VariableManager;

import java.lang.reflect.Type;
import java.util.List;

public class Project implements Parcelable {

    public static final String TAG = "FLOWCHART_PROJECT";
    public static final String PROJECT_NAME = "NAME";
    private Function currentScene;
    private Context context;
    private String name;
    private static AndroidPluginHandle pluginHandle;

    private FragmentManager fragmentManager;
    private VariableManager variableManager;
    private FunctionManager functionManager;
    private String gen;

    public Project(String name) {
        this.name = name;

        variableManager = new VariableManager(this);
        functionManager = new FunctionManager(this);
    }

    protected Project(Parcel in) {
        currentScene = in.readParcelable(Function.class.getClassLoader());
        name = in.readString();
        variableManager = in.readParcelable(VariableManager.class.getClassLoader());
        functionManager = in.readParcelable(FunctionManager.class.getClassLoader());
        gen = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public static Project create(String name) {
        return new Project(name);
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

    public Function getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Function currentScene) {
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

    public VariableManager getVariableManager() {
        return variableManager;
    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public void save() {
        Files.saveProject(this);
    }

    public static Type getSavingType() {
        return new TypeToken<List<Function.SimpleObject>>() {
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

        List<Function.SimpleObject> functions = Stream.of(functionManager.getFunctions())
                .map(Function::getSaveInfo).toList();

        List<Variable.SimpleObject> variables = Stream.of(variableManager.getVariables())
                .map(Variable::getSaveInfo).toList();

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
        final List<Function.SimpleObject> functions;
        final List<Variable.SimpleObject> variables;

        public SimpleObject(String plugin, List<Function.SimpleObject> functions,
                            List<Variable.SimpleObject> variables) {
            this.plugin = plugin;
            this.functions = functions;
            this.variables = variables;
        }
    }
}
