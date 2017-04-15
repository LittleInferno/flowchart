package com.littleinferno.flowchart.function;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.util.DestroyListener;
import com.littleinferno.flowchart.util.NameChangedListener;

import java.util.ArrayList;
import java.util.List;


public class AndroidFunction {

    private final AndroidFunctionManager functionManager;

    private String name;
    private List<FunctionParameter> parameters;

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

    public List<FunctionParameter> getParameters() {
        return parameters;
    }

    public List<FunctionParameter> getInputParameters() {
        return Stream.of(parameters).filter(value -> value.getConnection() == Connection.INPUT).toList();
    }

    public List<FunctionParameter> getOutputParameters() {
        return Stream.of(parameters).filter(value -> value.getConnection() == Connection.OUTPUT).toList();
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

    private void addParameter(FunctionParameter.FunctionParameterHandle parameterHandle) {
        addParameter(parameterHandle.name, parameterHandle.dataType,
                parameterHandle.connection, parameterHandle.isArray);
    }

    public FunctionParameter addParameter(String name, DataType type, Connection connection, boolean isArray) {
        FunctionParameter parameter =
                new FunctionParameter(new FunctionParameter
                        .FunctionParameterHandle(name, type, connection, isArray));

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

    private void notifyListenersParameterAdded(FunctionParameter parameter) {
        Stream.of(parameterAddedListeners).forEach(var -> var.add(parameter));
    }

    private void notifyListenersParameterRemoved(FunctionParameter parameter) {
        Stream.of(parameterRemovedListeners).forEach(var -> var.remove(parameter));
    }

    public void addReturnNode(FunctionReturnNode returnNode) {
        returnNodes.add(returnNode);
        returnNode.addCloseButton();
    }

    public void applyParameters() {
        if (!parameterAddedListeners.isEmpty()) {
            FunctionParameter.Added listener = parameterAddedListeners.get(parameterAddedListeners.size() - 1);
            Stream.of(parameters).forEach(listener::add);
        }
    }

    public String gen(BaseCodeGenerator builder) {
        return generateListener.gen(builder);
    }


    void destroy() {
        notifyListenersDestroed();
    }

    public interface GenerateListener {
        String gen(BaseCodeGenerator builder);
    }

}
