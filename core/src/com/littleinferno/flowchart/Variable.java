package com.littleinferno.flowchart;

import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.VariableGetNode;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

import java.util.ArrayList;

public class Variable {

    public Variable(String name) {
        this.name = name;
        this.nodes = new ArrayList<Node>();
    }

    public Value.Type getValueType() {
        return valueType;
    }

    public void setValueType(Value.Type valueType) {
        this.valueType = valueType;

        for (Node i : nodes) {

            if (i instanceof VariableGetNode)
                ((Pin) i.findActor("get")).setData(valueType);
            else
                ((Pin) i.findActor("set")).setData(valueType);
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
        nodes.remove(node);
    }

    private Value.Type valueType;
    private Value value;
    private String name;
    private ArrayList<Node> nodes;
}
