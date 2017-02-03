package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class PrintNode extends Node {

    private final Pin next;
    private final Pin value;

    public PrintNode() {
        super("Print", true);

        next = addExecutionOutputPin();
        addExecutionInputPin();

        value = addDataInputPin("item", DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        Pin.Connector val = value.getConnector();
        String valStr = val.parent.gen(builder, val.pin);

        Pin.Connector n = next.getConnector();
        String nextStr = n != null ? n.parent.gen(builder, n.pin) : "";

        return String.format("com.littleinferno.flowchart.codegen.IO.print(%s)\n%s", valStr, nextStr);
    }
}
