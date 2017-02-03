package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class MulNode extends ArithmeticNode {
    public MulNode() {
        super("mul", DataType.INT, DataType.FLOAT);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector aConnector = a.getConnector();
        Pin.Connector bConnector = b.getConnector();

        String aStr = aConnector.parent.gen(builder, aConnector.pin);
        String bStr = bConnector.parent.gen(builder, bConnector.pin);

        return builder.createMul(aStr, bStr);
    }
}
