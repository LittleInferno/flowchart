package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArrayGetNode extends Node {

    private DataType[] converts = {DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING};
    private final Pin array = addDataInputPin("array", converts);
    private final Pin item = addDataOutputPin("item", converts);


    public ArrayGetNode() {
        super("Get", true);

        addDataInputPin(DataType.INT, "index");

        Pin.PinListener listener = t -> {
            array.setType(t);
            item.setType(t);
        };

        array.setArray(true);
        array.addListener(listener);
        item.addListener(listener);

    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        Pin.Connector a = array.getConnector();
        Pin.Connector i = item.getConnector();
        return builder.createGetArrayItem(a.parent.gen(builder, a.pin),
                i.parent.gen(builder, i.pin));
    }
}
