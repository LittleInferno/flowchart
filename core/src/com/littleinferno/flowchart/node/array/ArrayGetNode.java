package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArrayGetNode extends Node {

    private Pin array;
    private Pin index;
    private Pin item;

    public ArrayGetNode() {
        this(new NodeHandle("Get", true));
    }

    public ArrayGetNode(NodeHandle nodeHandle) {
        super(nodeHandle);

        array = addDataInputPin("array", Pin.DEFAULT_CONVERT);
        index = addDataInputPin("index", DataType.INT);
        item = addDataOutputPin("item", Pin.DEFAULT_CONVERT);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector a = array.getConnector();
        Pin.Connector i = index.getConnector();
        return builder.makeGetArrayItem(a.parent.gen(builder, a.pin),
                i.parent.gen(builder, i.pin));
    }
}
