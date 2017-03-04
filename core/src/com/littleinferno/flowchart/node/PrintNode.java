package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class PrintNode extends Node {

    private Pin next;
    private Pin value;

    public PrintNode() {
        this(new NodeHandle("Print", true));
    }

    public PrintNode(NodeHandle nodeHandle) {
        super(nodeHandle);

        next = addExecutionOutputPin();
        addExecutionInputPin();

        value = addDataInputPin("item", Pin.DEFAULT_CONVERT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector val = value.getConnector();
        String valStr = val.parent.gen(builder, val.pin);

        Pin.Connector n = next.getConnector();
        String nextStr = n != null ? n.parent.gen(builder, n.pin) : "";

        String format = String.format("com.littleinferno.flowchart.jsutil.IO.print(%s)", valStr);

        return String.format("%s%s", builder.makeStatement(format), nextStr);
    }
}
