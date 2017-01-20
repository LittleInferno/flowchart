package com.littleinferno.flowchart;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.value.Value;

public class Variable {

    public Variable(String name) {
        this.name = name;
        this.nodes = new Array<Node>();
    }

    public Value.Type getValueType() {
        return valueType;
    }

    public void setValueType(Value.Type valueType) {
        this.valueType = valueType;

        for (Node i : nodes) {
            i.getPin("data").setType(valueType);
        }
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
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
        return String.format("var %s\n", name);
    }

    private Value.Type valueType;
    private Value value;
    private String name;
    private Array<Node> nodes;
}
