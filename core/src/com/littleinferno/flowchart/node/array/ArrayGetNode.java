package com.littleinferno.flowchart.node.array;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.pin.PinListener;

public class ArrayGetNode extends Node {

    private DataType[] converts = {DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING};
    private final Pin array = addDataInputPin("array", converts);
    private final Pin item = addDataOutputPin("item", converts);


    public ArrayGetNode() {
        super("Get", true);

        array.setArray(true);

        PinListener defaultListener = new PinListener() {
            @Override
            public void typeChanged(DataType newType) {
                array.setType(newType);
                item.setType(newType);
            }
        };

        array.addListener(defaultListener);
        item.addListener(defaultListener);

        addDataInputPin(DataType.INT, "index");
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        Pin.Connector a = array.getConnector();
        Pin.Connector i = item.getConnector();
        return builder.createGetArrayItem(a.parent.gen(builder, a.pin),
                i.parent.gen(builder, i.pin));
    }
}
