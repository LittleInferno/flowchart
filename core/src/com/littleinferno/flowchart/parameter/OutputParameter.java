package com.littleinferno.flowchart.parameter;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class OutputParameter extends Parameter {
    public OutputParameter(Function function, String name, Value.Type type) {
        super(function, name, type);
        function.getReturnNode().addDataInputPin(valueType, name);
    }

    @Override
    public void setName(String name) {
        function.getReturnNode().getItem(this.name).setName(name);

        Array<Node> nodes = function.getNodes();
        for (int i = 0; i < nodes.size; ++i) {
            nodes.get(i).getItem(this.name).setName(name);
        }

        this.name = name;
    }

    @Override
    public void setValueType(Value.Type valueType) {
        this.valueType = valueType;
        function.getReturnNode().getItem(this.name).getPin().setData(valueType);

        Array<Node> nodes = function.getNodes();
        for (int i = 0; i < nodes.size; ++i) {
            nodes.get(i).getItem(this.name).getPin().setData(valueType);
        }
    }
}
