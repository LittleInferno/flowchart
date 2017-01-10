package com.littleinferno.flowchart.node;


import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class VariableGetNode extends Node {

    public VariableGetNode(Vector2 pos, Variable variable) {
        super(pos, String.format("Get %s", variable.getName()));

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
