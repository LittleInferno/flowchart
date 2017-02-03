package com.littleinferno.flowchart;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.node.FunctionBeginNode;
import com.littleinferno.flowchart.node.FunctionCallNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.parameter.ParameterListener;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Function implements NameChangeable {

    private String name;

    private FunctionBeginNode beginNode;
    private List<FunctionReturnNode> returnNodes;
    private List<FunctionCallNode> callNodes;

    private Array<Parameter> parameters;
    private List<NameChange> nameChangeListeners;
    private List<ParameterListener> parameterListeners;


    public Function(String name) {
        this.name = name;
        nameChangeListeners = new ArrayList<NameChange>();
        parameterListeners = new ArrayList<ParameterListener>();

        parameters = new Array<Parameter>();

        //   Table functionWindow = Main.addWindow(name).getContentTable();
//        functionWindow.addActor(beginNode);

        beginNode = new FunctionBeginNode(this, Main.skin);
        beginNode.setPosition(100, 100);

        returnNodes = new ArrayList<FunctionReturnNode>();

        // FunctionReturnNode returnNode = new FunctionReturnNode(this, Main.skin);
        //  returnNode.setPosition(400, 100);
        //   returnNodes.add(returnNode);

        callNodes = new ArrayList<FunctionCallNode>();


    }

    public FunctionBeginNode getBeginNode() {
        return beginNode;
    }

    public Array<Parameter> getParameters() {
        return parameters;
    }

    public List<FunctionReturnNode> getReturnNodes() {
        return returnNodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        notifyListenersNameChanged(name);
    }

    public void addNode(FunctionCallNode node) {
        callNodes.add(node);

        if (!parameterListeners.isEmpty()) {
            ParameterListener listener = parameterListeners.get(parameterListeners.size() - 1);
            for (int i = 0; i < parameters.size; ++i) {
                notifyListenersParameterAdded(listener, parameters.get(i));
            }
        }
    }

    public void removeNode(Node node) {

        if (node instanceof FunctionBeginNode)
            beginNode = null;
        else if (node instanceof FunctionReturnNode)
            returnNodes.remove(node);
        else
            callNodes.remove(node);
    }

    public void addParameter(Parameter parameter) {
        notifyListenersParameterAdded(parameter);

        parameters.add(parameter);
    }

    public void removeParameter(Parameter parameter) {
        notifyListenersParameterRemoved(parameter);

        parameters.removeValue(parameter, true);
    }

    public List<FunctionCallNode> getNodes() {
        return callNodes;
    }

    @Override
    public void addListener(NameChange listener) {
        nameChangeListeners.add(listener);
    }

    @Override
    public void notifyListenersNameChanged(String newName) {
        for (NameChange listener : nameChangeListeners) {
            listener.changed(newName);
        }
    }

    public void addReturnNode(FunctionReturnNode returnNode) {
        returnNodes.add(returnNode);

        if (!parameterListeners.isEmpty()) {
            ParameterListener listener = parameterListeners.get(parameterListeners.size() - 1);
            for (int i = 0; i < parameters.size; ++i) {
                notifyListenersParameterAdded(listener, parameters.get(i));
            }
        }
    }

    public void addListener(ParameterListener listener) {
        parameterListeners.add(listener);
    }

    private void notifyListenersParameterAdded(ParameterListener listener, Parameter parameter) {
        listener.added(parameter);
    }

    private void notifyListenersParameterAdded(Parameter parameter) {
        for (ParameterListener listener : parameterListeners) {
            listener.added(parameter);
        }
    }

    private void notifyListenersParameterRemoved(Parameter parameter) {
        for (ParameterListener listener : parameterListeners) {
            listener.removed(parameter);
        }
    }


    public void delete() {

        // TODO
        //  removeNode(beginNode);

        for (Iterator<FunctionReturnNode> i = returnNodes.iterator(); i.hasNext(); ) {
            Node node = i.next();
            node.close();
            i.remove();
        }

        for (Iterator<FunctionCallNode> i = callNodes.iterator(); i.hasNext(); ) {
            Node node = i.next();
            node.close();
            i.remove();
        }
    }
}
