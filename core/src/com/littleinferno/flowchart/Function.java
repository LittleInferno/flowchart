package com.littleinferno.flowchart;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.node.FunctionBeginNode;
import com.littleinferno.flowchart.node.FunctionCallNode;
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
    private Array<Parameter> parameters;
    private FunctionCallNode currentCall;

    public Function(String name) {
        this.name = name;

        beginNode = new FunctionBeginNode(this, Main.skin);
        beginNode.setPosition(100, 100);
        returnNode = new FunctionReturnNode(this, Main.skin);
        returnNode.setPosition(400, 100);

        Table functionWindow = Main.addWindow(name).getContentTable();
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

    public void removeParameter(Parameter parameter) {
        if (parameter instanceof InputParameter)
            beginNode.removePin(parameter.getName());
        else
            returnNode.removePin(parameter.getName());
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    public FunctionCallNode getCurrentCall() {
        return currentCall;
    }

    public void setCurrentCall(FunctionCallNode currentCall) {
        this.currentCall = currentCall;
    }


    public void gen(){
//        function = new com.littleinferno.flowchart.codegen.Function(beginNode.genStatement(null)) {
//        };
    }
}
