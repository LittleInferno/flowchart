package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class MulNode extends ArithmeticNode {
    public MulNode() {
        super("mul", DataType.INT, DataType.FLOAT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeMul(a, b);
    }
}
