package com.littleinferno.flowchart;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Variable {

    private DataType dataType;
    private String name;
    private Array<Node> nodes;
    private boolean isArray;
    private List<VariableChangedListener> changedListeners;

    public Variable(String name, DataType type, boolean isArray) {
        this.name = name;
        this.dataType = type;
        this.isArray = isArray;
        this.nodes = new Array<Node>();
        this.changedListeners = new ArrayList<VariableChangedListener>();
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType newType) {
        this.dataType = newType;

        notifyListenersTypeChanged(newType);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;

        notifyListenersNameChanged(newName);
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.removeValue(node, true);
    }

    public String gen() {
        if (isArray)
            return String.format("var %s=[];\n", name);
        else
            return String.format("var %s;\n", name);
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;

        notifyListenersIsArrayChanged(isArray);
    }

    public void addListener(VariableChangedListener listener) {
        changedListeners.add(listener);
    }

    private void notifyListenersNameChanged(String newName) {
        for (VariableChangedListener listener : changedListeners) {
            listener.nameChanged(newName);
        }
    }

    private void notifyListenersTypeChanged(DataType newtype) {
        for (VariableChangedListener listener : changedListeners) {
            listener.typeChanged(newtype);
        }
    }

    private void notifyListenersIsArrayChanged(boolean isArray) {
        for (VariableChangedListener listener : changedListeners) {
            listener.isArrayChanged(isArray);
        }
    }
}
