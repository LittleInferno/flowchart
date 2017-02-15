package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class AddNode extends ArithmeticNode {

    public AddNode() {
        super("add", DataType.INT, DataType.FLOAT, DataType.STRING);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeAdd(a, b);
    }
}
