package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class GreatNode extends LogicNode {

    public GreatNode(NodeHandle nodeHandle) {
        super(nodeHandle, DataType.FLOAT, DataType.INT, DataType.STRING);
    }

    public GreatNode() {
        super(new NodeHandle("great", true), DataType.FLOAT, DataType.INT, DataType.STRING);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeGt(a, b);
    }
}
