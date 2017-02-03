package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class EqualNode extends LogicNode {
    public EqualNode() {
        super("equals", DataType.FLOAT, DataType.INT, DataType.STRING, DataType.BOOL);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector aConnector = a.getConnector();
        Pin.Connector bConnector = b.getConnector();

        String aStr = aConnector.parent.gen(builder, aConnector.pin);
        String bStr = bConnector.parent.gen(builder, bConnector.pin);

        return builder.createEq(aStr, bStr);
    }
}
