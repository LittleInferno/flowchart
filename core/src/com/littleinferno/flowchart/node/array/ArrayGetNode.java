package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArrayGetNode extends Node {

    private final Pin array;
    private final Pin index;
    private final Pin item;

    public ArrayGetNode() {
        super("Get", true);

        array = addDataInputPin("array", Pin.DEFAULT_CONVERT);
        index = addDataInputPin(DataType.INT, "index");
        item = addDataOutputPin("item", Pin.DEFAULT_CONVERT);

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
        Pin.Connector i = index.getConnector();
        return builder.makeGetArrayItem(a.parent.gen(builder, a.pin),
                i.parent.gen(builder, i.pin));
    }
}
