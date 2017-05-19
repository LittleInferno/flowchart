package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.generator.Generator;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.node.AndroidNodeManager;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AndroidFunction extends ProjectModule implements Generator, Parcelable {
    private final HashMap<UUID, AndroidFunctionManager> parent = new HashMap<>();

    public static final String TAG = "FUNCTION";

    private final AndroidFunctionManager functionManager;

    private String name;
    private List<AndroidFunctionParameter> parameters;

    private List<NameChangedListener> nameChangedListeners;
    private List<DestroyListener> destroyListeners;

    private final AndroidNodeManager nodeManager;
    private AndroidSceneLayout androidScene;
    private List<AndroidFunctionParameter.CallbackPair> parameterListeners = new ArrayList<>();
    private final List<Link> parameterAddListeners = new ArrayList<>();
    private final List<Link> parameterRemoveListeners = new ArrayList<>();

    AndroidFunction(AndroidFunctionManager functionManager, String name) {
        super(functionManager.getProject());

        this.functionManager = functionManager;
        this.name = name;

        parameters = new ArrayList<>();

        nameChangedListeners = new ArrayList<>();
        destroyListeners = new ArrayList<>();

        nodeManager = new AndroidNodeManager(this);
        nodeManager.createNode("function begin node", 10, 400);
    }


    AndroidFunction(AndroidFunctionManager functionManager, SimpleObject saveInfo) {
        super(functionManager.getProject());

        this.functionManager = functionManager;
        this.name = saveInfo.name;

        nodeManager = new AndroidNodeManager(this);
        parameters = new ArrayList<>();

        Stream.of(saveInfo.nodes).forEach(nodeManager::createNode);
        Stream.of(saveInfo.parameters).forEach(this::addParameter);
    }

    private AndroidFunction(Parcel in) {
        super(in);

        functionManager = parent.remove(getId());

        name = in.readString();
        nodeManager = in.readParcelable(AndroidNodeManager.class.getClassLoader());

        parameters = new ArrayList<>();
        in.readList(parameters, AndroidFunctionParameter.class.getClassLoader());

        parameterListeners = new ArrayList<>();
        in.readList(parameterListeners, Map.Entry.class.getClassLoader());
    }

    public static final Creator<AndroidFunction> CREATOR = new Creator<AndroidFunction>() {
        @Override
        public AndroidFunction createFromParcel(Parcel in) {
            return new AndroidFunction(in);
        }

        @Override
        public AndroidFunction[] newArray(int size) {
            return new AndroidFunction[size];
        }
    };

    public List<AndroidFunctionParameter> getParameters() {
        return Stream.of(parameters).toList();
    }

    @SuppressWarnings("unused")
    public AndroidFunctionParameter[] getInputParameters() {
        return Stream.of(getParameters())
                .filter(value -> value.getConnection() == Connection.INPUT)
                .toArray(AndroidFunctionParameter[]::new);
    }

    @SuppressWarnings("unused")
    public AndroidFunctionParameter[] getOutputParameters() {
        return Stream.of(getParameters())
                .filter(value -> value.getConnection() == Connection.OUTPUT)
                .toArray(AndroidFunctionParameter[]::new);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyListenersNameChanged(name);
    }

    public AndroidFunctionParameter addParameter(Connection connection, String name, DataType type, boolean isArray) {
        AndroidFunctionParameter parameter =
                new AndroidFunctionParameter(this, connection, type, name, isArray);

        parameters.add(parameter);

        if (nodeManager.getNodes("function return node").isEmpty()) {
            nodeManager.createNode("function return node", 600, 400);
        }

        notifyParameterAdd();
        notifyListenersParameterAdded(parameter);

        return parameter;
    }

    private AndroidFunctionParameter addParameter(AndroidFunctionParameter.SimpleObject savedInfo) {
        return addParameter(Connection.valueOf(savedInfo.connection), savedInfo.name,
                DataType.valueOf(savedInfo.type), savedInfo.array);
    }

    public void removeParameter(AndroidFunctionParameter parameter) {
        parameters.remove(parameter);
        notifyParameterRemove();
        notifyListenersParameterRemoved(parameter);
        if (getOutputParameters().length == 0)
            Stream.of(nodeManager.getNodes("function return node")).forEach(nodeManager::removeNode);
    }

    public void removeParameter(int position) {
        removeParameter(parameters.get(position));
    }

    @SuppressWarnings("unused")
    public void onNameChange(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void removeNameChangeListener(NameChangedListener listener) {
        nameChangedListeners.remove(listener);
    }

    @SuppressWarnings("unused")
    public int addParameterListener(AndroidFunctionParameter.Add add, AndroidFunctionParameter.Remove remove) {
        parameterListeners.add(new AndroidFunctionParameter.CallbackPair(add, remove));
        return parameterListeners.get(parameterListeners.size() - 1).hashCode();
    }

    @SuppressWarnings("unused")
    public void removeParameterListener(int i) {
        parameterListeners = Stream.of(parameterListeners).filter(v -> v.hashCode() != i).toList();
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    private void notifyListenersParameterAdded(AndroidFunctionParameter parameter) {
        Stream.of(parameterListeners).map(AndroidFunctionParameter.CallbackPair::getAdd)
                .forEach(v -> v.add(parameter));
    }

    private void notifyListenersParameterRemoved(AndroidFunctionParameter parameter) {
        Stream.of(parameterListeners).map(AndroidFunctionParameter.CallbackPair::getRemove)
                .forEach(v -> v.remove(parameter));
    }

    @SuppressWarnings("unused")
    public void applyParameters() {
        if (!parameterListeners.isEmpty()) {
            AndroidFunctionParameter.Add listener = parameterListeners.get(parameterListeners.size() - 1).getAdd();
            Stream.of(parameters).forEach(listener::add);
        }
    }

    void destroy() {
        notifyListenersDestroed();
    }

    public String checkParameterName(String name) {
        String result = functionManager.checkFunctionName(name);

        if (result == null) {
            if (Stream.of(parameters).map(AndroidFunctionParameter::getName).anyMatch(name::equals))
                return "This name is already taken";
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        parent.put(getId(), functionManager);

        dest.writeString(name);
        dest.writeParcelable(nodeManager, flags);
    }

    public AndroidNodeManager getNodeManager() {
        return nodeManager;
    }

    public void bindScene(AndroidSceneLayout androidScene) {
        this.androidScene = androidScene;

        Stream.of(nodeManager.getNodes())
                .forEach(androidNode -> {
                    AndroidSceneLayout parent = (AndroidSceneLayout) androidNode.getParent();
                    if (parent != null)
                        parent.removeView(androidNode);

                    androidScene.addView(androidNode);
                });
    }

    public AndroidFunctionManager getFunctionManager() {
        return functionManager;
    }

    public void nodeAdded(AndroidNode node) {
        if (androidScene != null)
            androidScene.addView(node);
    }

    public Link onParameterAdd(Fun fun) {
        Link link = new Link(parameterAddListeners, fun);
        parameterAddListeners.add(link);
        return link;
    }

    public Link onParameterRemove(Fun fun) {
        Link link = new Link(parameterRemoveListeners, fun);
        parameterRemoveListeners.add(link);
        return link;
    }

    private void notifyParameterAdd() {
        Stream.of(parameterAddListeners).forEach(Link::call);
    }

    private void notifyParameterRemove() {
        Stream.of(parameterRemoveListeners).forEach(Link::call);
    }

    public void updateData() {
        notifyParameterAdd();
    }

    public AndroidFunctionParameter getParameter(int position) {
        return parameters.get(position);
    }

    public SimpleObject getSaveInfo() {
        return new SimpleObject(name,
                Stream.of(parameters).map(AndroidFunctionParameter::getSaveInfo).toList(),
                Stream.of(nodeManager.getNodes()).map(AndroidNode::getSaveInfo).toList());
    }

    @Override
    public String generate() {
        AndroidNode node = nodeManager.getNode("function begin node");

        String functionGen = node.getNodeHandle()
                .getAttribute("functionGen")
                .map(org.mozilla.javascript.Function.class::cast)
                .map(node.getNodeHandle().getPluginHandle()::createScriptFun)
                .orElseThrow(RuntimeException::new)
                .call(String.class, node, this);

        Log.d(getName(), functionGen);
        return functionGen;
    }

    public static class SimpleObject {
        final String name;
        final List<AndroidFunctionParameter.SimpleObject> parameters;
        final List<AndroidNode.SimpleObject> nodes;

        public SimpleObject(String name, List<AndroidFunctionParameter.SimpleObject> parameters, List<AndroidNode.SimpleObject> nodes) {
            this.name = name;
            this.parameters = parameters;
            this.nodes = nodes;
        }
    }
}
