package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.variable.Variable;


public class VariableSetNode extends VariableNode {

    private final Pin pin;
    private final Pin next;

    public VariableSetNode(Variable variable) {
        super(variable, String.format("Set %s", variable.getName()));

        addExecutionInputPin();
        next = addExecutionOutputPin();

        this.pin = addDataInputPin("data", variable.getDataType());
        this.pin.setArray(variable.isArray());

        this.variable.addListener(this.pin::setArray);
        this.variable.addListener(this::setTitle);
        this.variable.addListener(this.pin::setType);
        this.variable.addListener(this::close);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector data = pin.getConnector();
        String dataStr = data.parent.gen(builder, data.pin);

        Pin.Connector n = next.getConnector();
        String nextStr = n == null ? "" : n.parent.gen(builder, n.pin);

        String s = builder.makeStatement(builder.makeAssign(variable.getName(), dataStr));

        return String.format("%s%s", s, nextStr);
    }
}
