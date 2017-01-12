package com.littleinferno.flowchart;

import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.node.FunctionBeginNode;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.ui.FunctionWindow;
import com.littleinferno.flowchart.value.Value;

import java.util.ArrayList;

public class Function {

    public enum ParameterType {
        INPUT,
        OUTPUT
    }

    public class Parameter {

        public Parameter(ParameterType type, Value.Type valueType, String name) {
            this.valueType = valueType;
            this.name = name;
            this.type = type;

            if (type == ParameterType.INPUT)
                beginNode.addDataOutputPin(valueType, name);
            else
                returnNode.addDataInputPin(valueType, name);
        }

        public Value.Type getValueType() {
            return valueType;
        }

        public void setValueType(Value.Type valueType) {
            this.valueType = valueType;

            if (type == ParameterType.INPUT)
                beginNode.getItem(getName()).getPin().setData(valueType);
            else
                returnNode.getItem(getName()).getPin().setData(valueType);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (type == ParameterType.INPUT)
                beginNode.getItem(getName()).setName(name);
            else
                returnNode.getItem(getName()).setName(name);

            this.name = name;
        }

        private Value.Type valueType;
        private String name;
        private ParameterType type;
    }

    public Function(String name) {
        this.name = name;

        beginNode = new FunctionBeginNode(new Vector2(100, 100), this);
        returnNode = new FunctionReturnNode(new Vector2(400, 100), this);
    }

    public void setWindow(FunctionWindow functionWindow) {
        this.functionWindow = functionWindow;

        functionWindow.addActor(beginNode);
        functionWindow.addActor(returnNode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addParameter(Parameter parameter) {
    }

    //public void addInputParametr()

    private String name;
    private ArrayList<Node> nodes;
    private FunctionBeginNode beginNode;
    private FunctionReturnNode returnNode;
    private FunctionWindow functionWindow;
}
