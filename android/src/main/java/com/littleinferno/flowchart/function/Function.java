package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.util.Connection;
import com.littleinferno.flowchart.util.DataType;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Generator;
import com.littleinferno.flowchart.util.Link;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Function extends ProjectModule implements Generator, Parcelable {
    private final HashMap<UUID, FunctionManager> parent = new HashMap<>();

    public static final String TAG = "FUNCTION";

    private final FunctionManager functionManager;

    private String name;
    private List<FunctionParameter> parameters;

    private List<NameChangedListener> nameChangedListeners;
    private List<DestroyListener> destroyListeners;

    private final NodeManager nodeManager;
    private AndroidSceneLayout scene;
    private List<FunctionParameter.CallbackPair> parameterListeners = new ArrayList<>();
    private final List<Link> parameterAddListeners = new ArrayList<>();
    private final List<Link> parameterRemoveListeners = new ArrayList<>();
    private List<AndroidSceneLayout.Wire.SimpleObject> wires = Collections.emptyList();

    Function(FunctionManager functionManager, String name) {
        super(functionManager.getProject());

        this.functionManager = functionManager;
        this.name = name;

        parameters = new ArrayList<>();

        nameChangedListeners = new ArrayList<>();
        destroyListeners = new ArrayList<>();

        nodeManager = new NodeManager(this);
        nodeManager.createNode("function begin node", 10, 400);
    }


    Function(FunctionManager functionManager, SimpleObject saveInfo) {
        super(functionManager.getProject());

        this.functionManager = functionManager;
        this.name = saveInfo.name;

        nodeManager = new NodeManager(this);
        parameters = new ArrayList<>();

        Stream.of(saveInfo.nodes).forEach(nodeManager::createNode);
        Stream.of(saveInfo.parameters).forEach(this::addParameter);

        wires = saveInfo.wires;
    }

    private Function(Parcel in) {
        super(in);

        functionManager = parent.remove(getId());

        name = in.readString();
        nodeManager = in.readParcelable(NodeManager.class.getClassLoader());

        parameters = new ArrayList<>();
        in.readList(parameters, FunctionParameter.class.getClassLoader());

        parameterListeners = new ArrayList<>();
        in.readList(parameterListeners, Map.Entry.class.getClassLoader());
    }

    public static final Creator<Function> CREATOR = new Creator<Function>() {
        @Override
        public Function createFromParcel(Parcel in) {
            return new Function(in);
        }

        @Override
        public Function[] newArray(int size) {
            return new Function[size];
        }
    };

    public List<FunctionParameter> getParameters() {
        return Stream.of(parameters).toList();
    }

    @SuppressWarnings("unused")
    public FunctionParameter[] getInputParameters() {
        return Stream.of(getParameters())
                .filter(value -> value.getConnection() == Connection.INPUT)
                .toArray(FunctionParameter[]::new);
    }

    @SuppressWarnings("unused")
    public FunctionParameter[] getOutputParameters() {
        return Stream.of(getParameters())
                .filter(value -> value.getConnection() == Connection.OUTPUT)
                .toArray(FunctionParameter[]::new);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyListenersNameChanged(name);
    }

    public FunctionParameter addParameter(Connection connection, String name, DataType type, boolean isArray) {
        FunctionParameter parameter =
                new FunctionParameter(this, connection, type, name, isArray);

        parameters.add(parameter);

        if (nodeManager.getNodes("function return node").isEmpty()) {
            nodeManager.createNode("function return node", 600, 400);
        }

        notifyParameterAdd();
        notifyListenersParameterAdded(parameter);

        return parameter;
    }

    private FunctionParameter addParameter(FunctionParameter.SimpleObject savedInfo) {
        return addParameter(Connection.valueOf(savedInfo.connection), savedInfo.name,
                DataType.valueOf(savedInfo.type), savedInfo.array);
    }

    public void removeParameter(FunctionParameter parameter) {
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
    public FunctionParameter.CallbackPair addParameterListener(FunctionParameter.Add add, FunctionParameter.Remove remove) {
        parameterListeners.add(new FunctionParameter.CallbackPair(add, remove));
        return parameterListeners.get(parameterListeners.size() - 1);
    }

    @SuppressWarnings("unused")
    public void removeParameterListener(FunctionParameter.CallbackPair i) {
        parameterListeners = Stream.of(parameterListeners).filter(i::equals).toList();
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    private void notifyListenersParameterAdded(FunctionParameter parameter) {
        Stream.of(parameterListeners).map(FunctionParameter.CallbackPair::getAdd)
                .forEach(v -> v.add(parameter));
    }

    private void notifyListenersParameterRemoved(FunctionParameter parameter) {
        Stream.of(parameterListeners).map(FunctionParameter.CallbackPair::getRemove)
                .forEach(v -> v.remove(parameter));
    }

    @SuppressWarnings("unused")
    public void applyParameters() {
        if (!parameterListeners.isEmpty()) {
            FunctionParameter.Add listener = parameterListeners.get(parameterListeners.size() - 1).getAdd();
            Stream.of(parameters).forEach(listener::add);
        }
    }

    void destroy() {
        notifyListenersDestroed();
    }

    public String checkParameterName(String name) {
        String result = functionManager.checkFunctionName(name);

        if (result == null) {
            if (Stream.of(parameters).map(FunctionParameter::getName).anyMatch(name::equals))
                return "This name is already taken";
        }

        return result;
    }

    public void loadNodes() {
        Stream.of(nodeManager.getNodes()).forEach(AndroidNode::load);
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

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public void bindScene(AndroidSceneLayout androidScene) {
        this.scene = androidScene;

        Stream.of(nodeManager.getNodes())
                .forEach(androidNode -> {
                    AndroidSceneLayout parent = (AndroidSceneLayout) androidNode.getParent();
                    if (parent != null)
                        parent.removeView(androidNode);

                    androidScene.addView(androidNode);
                });

        Stream.of(wires).forEach(simpleObject -> {

            Pin begin = nodeManager
                    .getNode(UUID.fromString(simpleObject.begNode))
                    .getPin(simpleObject.begPin);

            Pin end = nodeManager
                    .getNode(UUID.fromString(simpleObject.endNode))
                    .getPin(simpleObject.endPin);

            begin.connect(end);
        });

    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public void nodeAdded(AndroidNode node) {
        if (scene != null)
            scene.addView(node);
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

    public FunctionParameter getParameter(int position) {
        return parameters.get(position);
    }

    public SimpleObject getSaveInfo() {
        return new SimpleObject(name,
                Stream.of(parameters).map(FunctionParameter::getSaveInfo).toList(),
                Stream.of(nodeManager.getNodes()).map(AndroidNode::getSaveInfo).toList(),
                scene.getSaveInfo());
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
        final List<FunctionParameter.SimpleObject> parameters;
        final List<AndroidNode.SimpleObject> nodes;
        final List<AndroidSceneLayout.Wire.SimpleObject> wires;

        public SimpleObject(String name,
                            List<FunctionParameter.SimpleObject> parameters,
                            List<AndroidNode.SimpleObject> nodes,
                            List<AndroidSceneLayout.Wire.SimpleObject> wires) {
            this.name = name;
            this.parameters = parameters;
            this.nodes = nodes;
            this.wires = wires;
        }
    }
}
