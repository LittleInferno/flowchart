package com.littleinferno.flowchart.function;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.project.FlowchartProject;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.project.ProjectModule;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.ArrayList;
import java.util.List;


public class AndroidFunction implements ProjectModule, Parcelable {

    public static final String TAG = "FUNCTION";

    private final AndroidFunctionManager functionManager;

    private String name;
    private List<AndroidFunctionParameter> parameters;

    private List<FunctionReturnNode> returnNodes;

    private List<NameChangedListener> nameChangedListeners;
    private List<DestroyListener> destroyListeners;

    private List<FunctionParameter.Added> parameterAddedListeners;
    private List<FunctionParameter.Removed> parameterRemovedListeners;
    private GenerateListener generateListener;

    public AndroidFunction(AndroidFunctionManager functionManager, String name) {

        this.functionManager = functionManager;
        this.name = name;

        parameters = new ArrayList<>();

        nameChangedListeners = new ArrayList<>();
        destroyListeners = new ArrayList<>();

        parameterAddedListeners = new ArrayList<>();
        parameterRemovedListeners = new ArrayList<>();

        returnNodes = new ArrayList<>();

    }

    protected AndroidFunction(Parcel in) {
        functionManager = in.readParcelable(AndroidFunctionManager.class.getClassLoader());
        name = in.readString();
        parameters = new ArrayList<>();
        in.readList(parameters, AndroidFunctionParameter.class.getClassLoader());
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

    public AndroidFunctionParameter addParameter(Connection connection, String name, DataType type, boolean isArray) {
        AndroidFunctionParameter parameter =
                new AndroidFunctionParameter(this, connection, type, name, isArray);

        notifyListenersParameterAdded(parameter);
        parameters.add(parameter);

        return parameter;
    }

    public void removeParameter(FunctionParameter parameter) {
        notifyListenersParameterRemoved(parameter);
        parameters.remove(parameter);
    }

    public void addListener(NameChangedListener listener) {
        nameChangedListeners.add(listener);
    }

    public void addListener(DestroyListener listener) {
        destroyListeners.add(listener);
    }

    public void addPareameterListener(FunctionParameter.Added added, FunctionParameter.Removed removed) {
        parameterAddedListeners.add(added);
        parameterRemovedListeners.add(removed);
    }

    public void setGenerateListener(GenerateListener listener) {
        generateListener = listener;
    }

    private void notifyListenersNameChanged(String newName) {
        Stream.of(nameChangedListeners).forEach(var -> var.changed(newName));
    }

    private void notifyListenersDestroed() {
        Stream.of(destroyListeners).forEach(DestroyListener::destroyed);
    }

    private void notifyListenersParameterAdded(AndroidFunctionParameter parameter) {
        //Stream.of(parameterAddedListeners).forEach(var -> var.add(parameter));
    }

    private void notifyListenersParameterRemoved(FunctionParameter parameter) {
        Stream.of(parameterRemovedListeners).forEach(var -> var.remove(parameter));
    }

    public void addReturnNode(FunctionReturnNode returnNode) {
        returnNodes.add(returnNode);
        returnNode.addCloseButton();
    }

    public void applyParameters() {
//        if (!parameterAddedListeners.isEmpty()) {
//            FunctionParameter.Added listener = parameterAddedListeners.get(parameterAddedListeners.size() - 1);
//            Stream.of(parameters).forEach(listener::add);
//        }
    }

    public String gen(BaseCodeGenerator builder) {
        return generateListener.gen(builder);
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
    public FlowchartProject getProject() {
        return functionManager.getProject();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(functionManager, flags);
        dest.writeString(name);
    }

    public interface GenerateListener {
        String gen(BaseCodeGenerator builder);
    }

}
