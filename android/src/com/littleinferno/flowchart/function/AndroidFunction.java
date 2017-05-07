package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.AndroidNode;
import com.littleinferno.flowchart.node.AndroidNodeManager;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.scene.AndroidSceneLayout;
import com.littleinferno.flowchart.scene.SceneType;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.Fun;
import com.littleinferno.flowchart.util.Link;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidFunction implements ProjectModule, Parcelable {

    public static final String TAG = "FUNCTION";

    private final AndroidFunctionManager functionManager;

    private String name;
    private List<AndroidFunctionParameter> parameters;

    private List<FunctionReturnNode> returnNodes;

    private List<NameChangedListener> nameChangedListeners;
    private List<DestroyListener> destroyListeners;

    private final AndroidNodeManager nodeManager;
    private AndroidSceneLayout androidScene;
    private List<Map.Entry<AndroidFunctionParameter.Add, AndroidFunctionParameter.Remove>> parameterListeners;
    private final List<Link> parameterAddListeners;
    private final List<Link> parameterRemoveListeners;

    AndroidFunction(AndroidFunctionManager functionManager, String name) {

        this.functionManager = functionManager;
        this.name = name;

        parameters = new ArrayList<>();

        nameChangedListeners = new ArrayList<>();
        destroyListeners = new ArrayList<>();

        parameterListeners = new ArrayList<>();

        returnNodes = new ArrayList<>();

        nodeManager = new AndroidNodeManager(SceneType.FUNCTION, this);

        AndroidNode begin = nodeManager.createNode("function begin node");
        begin.setX(10);
        begin.setY(400);

        AndroidNode end = nodeManager.createNode("function return node");
        end.setX(600);
        end.setY(400);

        parameterAddListeners = new ArrayList<>();
        parameterRemoveListeners = new ArrayList<>();
    }

    private AndroidFunction(Parcel in) {
        name = in.readString();

        functionManager = in.readParcelable(AndroidFunctionManager.class.getClassLoader());
        nodeManager = in.readParcelable(AndroidNodeManager.class.getClassLoader());

        parameters = new ArrayList<>();
        in.readList(parameters, AndroidFunctionParameter.class.getClassLoader());

        parameterListeners = new ArrayList<>();
        in.readList(parameterListeners, Map.Entry.class.getClassLoader());

        parameterAddListeners = new ArrayList<>();
        in.readList(parameterAddListeners, Link.class.getClassLoader());

        parameterRemoveListeners = new ArrayList<>();
        in.readList(parameterRemoveListeners, Link.class.getClassLoader());
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
        return parameters;
    }

    public List<AndroidFunctionParameter> getInputParameters() {
        // return Stream.of(parameters).filter(value -> value.getConnection() == Connection.INPUT).toList();
        return null;
    }

    public List<AndroidFunctionParameter> getOutputParameters() {
        //    return Stream.of(parameters).filter(value -> value.getConnection() == Connection.OUTPUT).toList();
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyListenersNameChanged(name);
    }

    public void removeReturnNode(FunctionReturnNode node) {
        returnNodes.remove(node);
        if (returnNodes.size() == 1)
            returnNodes.get(0).removeCloseButton();
    }

    public AndroidFunctionParameter createParameter(Connection connection, String name, DataType type, boolean isArray) {
        AndroidFunctionParameter parameter =
                new AndroidFunctionParameter(this, connection, type, name, isArray);

        notifyListenersParameterAdded(parameter);
        parameters.add(parameter);
        notifyParameterAdd();
        return parameter;
    }

    public void removeParameter(AndroidFunctionParameter parameter) {
        notifyListenersParameterRemoved(parameter);
        parameters.remove(parameter);
        notifyParameterRemove();
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
        parameterListeners.add(new AbstractMap.SimpleEntry<>(add, remove));
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
        Stream.of(parameterListeners).map(Map.Entry::getKey).forEach(v -> v.add(parameter));
    }

    private void notifyListenersParameterRemoved(AndroidFunctionParameter parameter) {
        Stream.of(parameterListeners).map(Map.Entry::getValue).forEach(v -> v.remove(parameter));
    }

    public void addReturnNode(FunctionReturnNode returnNode) {
        returnNodes.add(returnNode);
        returnNode.addCloseButton();
    }

    @SuppressWarnings("unused")
    public void applyParameters() {
        if (!parameterListeners.isEmpty()) {
            AndroidFunctionParameter.Add listener = parameterListeners.get(parameterListeners.size() - 1).getKey();
            Stream.of(parameters).forEach(listener::add);
        }
    }

//    public String gen(BaseCodeGenerator builder) {
//        return generateListener.gen(builder);
//    }


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
    public FlowchartProject getProject() {
        return functionManager.getProject();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(functionManager, flags);
        dest.writeParcelable(nodeManager, flags);
        dest.writeList(parameterListeners);
        dest.writeList(parameterAddListeners);
        dest.writeList(parameterRemoveListeners);
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

    public void removeParameter(int position) {
        parameters.remove(position);
    }
}
