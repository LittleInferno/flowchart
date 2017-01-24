package com.littleinferno.flowchart.parameter;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.node.Node;

public class InputParameter extends Parameter {
    public InputParameter(Function function, String name, DataType type) {
        super(function, name, type);
        function.getBeginNode().addDataOutputPin(valueType, name);
    }

    @Override
    public void setName(String name) {
        function.getBeginNode().getPin(this.name).setName(name);

        Array<Node> nodes = function.getNodes();
        for (int i = 0; i < nodes.size; ++i) {
            nodes.get(i).getPin(this.name).setName(name);
        }

        this.name = name;
    }

    @Override
    public void setValueType(DataType valueType) {
        this.valueType = valueType;
        function.getBeginNode().getPin(this.name).setType(valueType);

        Array<Node> nodes = function.getNodes();
        for (int i = 0; i < nodes.size; ++i) {
            nodes.get(i).getPin(this.name).setType(valueType);
        }
    }
}
