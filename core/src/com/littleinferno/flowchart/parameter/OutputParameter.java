package com.littleinferno.flowchart.parameter;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.node.FunctionReturnNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class OutputParameter extends Parameter {
    public OutputParameter(Function function, String name, Value.Type type) {
        super(function, name, type);

        Array<FunctionReturnNode> nodes = function.getReturnNodes();
        for (Node i : nodes) {
            i.addDataInputPin(valueType, name);
        }
    }

    @Override
    public void setName(String name) {
        Array<FunctionReturnNode> returnNodes = function.getReturnNodes();
        for (Node i : returnNodes) {
            i.getPin(this.name).setName(name);
        }

        Array<Node> nodes = function.getNodes();
        for (int i = 0; i < nodes.size; ++i) {
            nodes.get(i).getPin(this.name).setName(name);

        }

        this.name = name;
    }

    @Override
    public void setValueType(Value.Type valueType) {
        this.valueType = valueType;

        Array<FunctionReturnNode> returnNodes = function.getReturnNodes();
        for (Node i : returnNodes) {
            i.getPin(this.name).setType(valueType);
        }

        Array<Node> nodes = function.getNodes();
        for (int i = 0; i < nodes.size; ++i) {
            nodes.get(i).getPin(this.name).setType(valueType);

        }
    }
}
