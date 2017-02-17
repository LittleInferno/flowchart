package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class LessNode extends LogicNode {
    public LessNode() {
        super("less", DataType.FLOAT, DataType.INT, DataType.STRING);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeLt(a, b);
    }
}