package com.littleinferno.flowchart.project;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.google.gson.reflect.TypeToken;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.function.FunctionManager;
import com.littleinferno.flowchart.plugin.PluginHandle;
import com.littleinferno.flowchart.util.Files;
import com.littleinferno.flowchart.util.Generator;
import com.littleinferno.flowchart.variable.Variable;
import com.littleinferno.flowchart.variable.VariableManager;

import java.lang.reflect.Type;
import java.util.List;

public class Project implements Parcelable, Generator {

    public static final String TAG = "FLOWCHART_PROJECT";
    public static final String PROJECT_NAME = "NAME";
    public static final String PLUGIN = "PLUGIN";
    private Function currentScene;
    private Context context;
    private String name;
    private static PluginHandle pluginHandle;

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

    public static Project create(Context context, String name) {
        Project project = new Project(name);
        project.setContext(context);
        return project;
    }

    public static Project load(Context context, String name) throws Exception {
        Project project = create(context, name);
        project.init(Files.loadProjectSave(name));
        return project;
    }

    public void setPlugin(PluginHandle plugin) {
        pluginHandle = plugin;
    }

    public void unloadPlugin() {
        pluginHandle.unload();
        pluginHandle = null;
    }

    public PluginHandle.NodeHandle getNodeHandle(final String nodeName) {
        return Stream.of(pluginHandle.getNodes())
                .filter(value -> value.getName().equals(nodeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find node: " + nodeName));
    }

    public List<PluginHandle.NodeHandle> getNodeHandles() {
        return pluginHandle.getNodes();
    }

    public PluginHandle.RuleHandle getRules() {
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
        Files.saveGen(this, gen);
    }

    public SimpleObject getSaveInfo() {

        List<Function.SimpleObject> functions = Stream.of(functionManager.getFunctions())
                .map(Function::getSaveInfo).toList();

        List<Variable.SimpleObject> variables = Stream.of(variableManager.getVariables())
                .map(Variable::getSaveInfo).toList();

        return new SimpleObject(pluginHandle.getPluginParams().getPluginName(), functions, variables);
    }

    private void init(SimpleObject saveInfo) throws Exception {

        setPlugin(Files.loadPlugin(context, saveInfo.plugin));
        Stream.of(saveInfo.variables).forEach(variableManager::createVariable);
        Stream.of(saveInfo.functions)
                .map(functionManager::createFunction)
                .forEach(Function::loadNodes);
    }

    @Override
    public String generate() {
        String variables = variableManager.generate();
        String functions = functionManager.generate();
        setGen(variables + "\n" + functions);
        return getGen();
    }

    public String getGen() {
        return gen;
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
