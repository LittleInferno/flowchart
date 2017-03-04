package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class LessNode extends LogicNode {

    public LessNode(NodeHandle nodeHandle) {
        super(nodeHandle, DataType.FLOAT, DataType.INT, DataType.STRING);
    }

    public LessNode() {
        super(new NodeHandle("less", true), DataType.FLOAT, DataType.INT, DataType.STRING);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeLt(a, b);
    }
}