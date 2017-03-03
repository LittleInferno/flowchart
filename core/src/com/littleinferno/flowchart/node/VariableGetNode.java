package com.littleinferno.flowchart.node;


import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.variable.Variable;

public class VariableGetNode extends VariableNode {

    public VariableGetNode(Variable variable) {
        super(variable, String.format("Get %s", variable.getName()));

        final Pin pin = addDataOutputPin("data", variable.getDataType());
        pin.setArray(variable.isArray());

        this.variable.addListener(pin::setArray);
        this.variable.addListener(this::setTitle);
        this.variable.addListener(pin::setType);
        this.variable.addListener(this::close);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeExpr(variable.getName());
    }
}
