package com.littleinferno.flowchart;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.VariableGetNode;

public class Variable {

    public Variable(String name) {
        this.name = name;
        this.nodes = new Array<Node>();
    }

    public DataType getValueType() {
        return valueType;
    }

    public void setValueType(DataType valueType) {
        this.valueType = valueType;

        for (Node i : nodes) {
            i.getPin("data").setType(valueType);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        for (Node i : nodes) {
            if (i instanceof VariableGetNode)
                i.setTitle(String.format("Get %s", name));
            else
                i.setTitle(String.format("Set %s", name));
        }
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

    private DataType valueType;
    private String name;
    private Array<Node> nodes;
    private boolean isArray;

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;

        for (Node i : nodes) {
            i.getPin("data").setArray(isArray);
        }

    }
}
