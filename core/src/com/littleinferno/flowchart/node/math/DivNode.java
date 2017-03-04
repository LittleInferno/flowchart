package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class DivNode extends ArithmeticNode {

    public DivNode(NodeHandle nodeHandle) {
        super(nodeHandle, DataType.INT, DataType.FLOAT);
    }

    public DivNode() {
        super(new NodeHandle("div", true), DataType.INT, DataType.FLOAT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeDiv(a, b);
    }
}