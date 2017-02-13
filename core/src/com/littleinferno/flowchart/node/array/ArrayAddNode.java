package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArrayAddNode extends Node {

    private DataType[] converts = {DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING};
    private final Pin next;
    private final Pin array;
    private final Pin item;
    private final Pin length;

    public ArrayAddNode() {
        super("Add", true);

        next = addExecutionOutputPin();
        addExecutionInputPin();
        array = addDataInputPin("array", converts);
        array.setArray(true);

        item = addDataInputPin("item", converts);
        length = addDataOutputPin(DataType.INT, "length");

        Pin.PinListener listener = t -> {
            array.setType(t);
            item.setType(t);
        };

        array.addListener(listener);
        item.addListener(listener);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        if (with == length) {
            Pin.Connector len = length.getConnector();
            return builder.makeGetArrayLength(len.parent.gen(builder, len.pin));
        }

        Pin.Connector arr = array.getConnector();
        Pin.Connector val = item.getConnector();

        String add = builder.makeAddArrayItem(arr.parent.gen(builder, arr.pin),
                val.parent.gen(builder, val.pin));

        Pin.Connector n = next.getConnector();
        String nextStr = n != null ? n.parent.gen(builder, n.pin) : "";

        return String.format("%s%s", add, nextStr);
    }
}
