package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArraySetNode extends Node {

    private Pin next;
    private Pin array;
    private Pin index;
    private Pin item;

    public ArraySetNode() {
        this(new Node.NodeHandle("Get", true));
    }

    public ArraySetNode(Node.NodeHandle nodeHandle) {
        super(nodeHandle);

        next = addExecutionOutputPin();

        addExecutionInputPin();
        array = addDataInputPin("array", Pin.DEFAULT_CONVERT);
        item = addDataInputPin("item", Pin.DEFAULT_CONVERT);
        index = addDataInputPin("index", DataType.INT);

        Pin.PinListener listener = t -> {
            array.setType(t);
            item.setType(t);
        };

        array.setArray(true);
        array.addListener(listener);
        item.addListener(listener);

    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector a = array.getConnector();
        Pin.Connector it = index.getConnector();
        Pin.Connector ix = index.getConnector();

        String s = builder.makeSetArrayItem(a.parent.gen(builder, a.pin),
                it.parent.gen(builder, it.pin)
                ,ix.parent.gen(builder, ix.pin));

        Pin.Connector n = next.getConnector();
        String nextStr = n != null ? n.parent.gen(builder, n.pin) : "";

        return String.format("%s%s", s, nextStr);
    }

}
