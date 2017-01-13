package com.littleinferno.flowchart.node;


import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.value.Value;

public class VariableGetNode extends Node {

    public VariableGetNode(Variable variable) {
        super(String.format("Get %s", variable.getName()), true);

        this.variable = variable;
        variable.addNode(this);

        addDataOutputPin(variable.getValueType(), "get");
    }

    @Override
    Value evaluate() {
        return variable.getValue();
    }

    private Variable variable;
}
