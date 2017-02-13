package com.littleinferno.flowchart.node;


import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.variable.Variable;

public class VariableGetNode extends Node {

    private final Variable variable;

    public VariableGetNode(Variable variable) {
        super(String.format("Get %s", variable.getName()), true);

        final Pin pin = addDataOutputPin(variable.getDataType(), "data");
        pin.setArray(variable.isArray());

        this.variable = variable;

        this.variable.addListener(pin::setArray);
        this.variable.addListener(this::setTitle);
        this.variable.addListener(pin::setType);
        this.variable.addListener(this::close);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return variable.getName();
    }
}
