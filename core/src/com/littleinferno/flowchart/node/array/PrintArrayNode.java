package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class PrintArrayNode extends Node {

    private Pin next;
    private Pin value;

    public PrintArrayNode(NodeHandle nodeHandle) {
        super(nodeHandle);
    }

    public PrintArrayNode() {
        this(new NodeHandle("print array", true));

        next = addExecutionOutputPin();
        addExecutionInputPin();

        value = addDataInputPin("items", Pin.DEFAULT_CONVERT);
        value.setArray(true);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector val = value.getConnector();
        String valStr = val.parent.gen(builder, val.pin);

        Pin.Connector n = next.getConnector();
        String nextStr = n != null ? n.parent.gen(builder, n.pin) : "";

        String format = String.format("com.littleinferno.flowchart.jsutil.IO.printArray(%s)", valStr);

        return String.format("%s%s", builder.makeStatement(format), nextStr);
    }
}
