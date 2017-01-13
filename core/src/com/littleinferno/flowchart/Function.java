package com.littleinferno.flowchart;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.node.FunctionBeginNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.parameter.InputParameter;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.ui.Main;

public class Function {

    private String name;
    private Array<Node> nodes;
    private FunctionBeginNode beginNode;
    private FunctionReturnNode returnNode;
    private Table functionWindow;
    private Array<Parameter> parameters;

    public Function(String name) {
        this.name = name;

        beginNode = new FunctionBeginNode(this);
        beginNode.setPosition(100, 100);
        returnNode = new FunctionReturnNode(this);
        returnNode.setPosition(400, 100);

        functionWindow = Main.addWindow(name).getContentTable();
        functionWindow.addActor(beginNode);
        functionWindow.addActor(returnNode);

        nodes = new Array<Node>();
        parameters = new Array<Parameter>();
    }

    public FunctionBeginNode getBeginNode() {
        return beginNode;
    }

    public FunctionReturnNode getReturnNode() {
        return returnNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        for (Node i : nodes) {
            i.setTitle(String.format("call %s", name));
        }

        beginNode.setTitle(name);
        returnNode.setTitle(name);
    }

    public void addNode(Node node) {
        nodes.add(node);

        for (int i = 0; i < parameters.size; ++i) {
            Parameter parameter = parameters.get(i);
            if (parameter instanceof InputParameter)
                node.addDataInputPin(parameter.getValueType(), parameter.getName());
            else
                node.addDataOutputPin(parameter.getValueType(), parameter.getName());
        }
    }

    public void removeNode(Node node) {
        nodes.removeValue(node, true);
    }

    public void addParameter(Parameter parameter) {
        for (int i = 0; i < nodes.size; ++i) {
            if (parameter instanceof InputParameter)
                nodes.get(i).addDataInputPin(parameter.getValueType(), parameter.getName());
            else
                nodes.get(i).addDataOutputPin(parameter.getValueType(), parameter.getName());
        }

        parameters.add(parameter);
    }

    public Array<Node> getNodes() {
        return nodes;
    }
}
