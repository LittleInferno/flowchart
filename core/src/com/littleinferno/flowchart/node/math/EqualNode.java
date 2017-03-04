package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class EqualNode extends LogicNode {

    public EqualNode(NodeHandle nodeHandle) {
        super(nodeHandle, Pin.DEFAULT_CONVERT);
    }

    public EqualNode() {
        super(new NodeHandle("equals", true), Pin.DEFAULT_CONVERT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeEq(a, b);
    }
}
