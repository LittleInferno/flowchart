package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class EqualNode extends LogicNode {
    public EqualNode() {
        super("equals", Pin.DEFAULT_CONVERT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeEq(a, b);
    }
}
