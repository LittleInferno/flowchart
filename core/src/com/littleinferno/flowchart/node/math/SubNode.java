package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class SubNode extends ArithmeticNode {
    public SubNode() {
        super("sub", DataType.INT, DataType.FLOAT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeSub(a, b);
    }
}
