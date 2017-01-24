package com.littleinferno.flowchart.node.array;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArrayAddNode extends Node {

    private Pin next;
    private Pin array;
    private Pin item;
    private Pin length;

    public ArrayAddNode(DataType type, Skin skin) {
        super("Add", true, skin);

        addExecutionInputPin();
        next = addExecutionOutputPin();
        array = addDataInputPin(type, "array");
        array.setArray(true);
        item = addDataInputPin(type, "index");
        length = addDataOutputPin(DataType.INT, "length");

    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        if (with == length) {
            Pin.Connector len = length.getConnector();
            return builder.createGetArrayLength(len.parent.gen(builder, len.pin));
        }

        Pin.Connector arr = array.getConnector();
        Pin.Connector val = item.getConnector();

        String add = builder.createAddItemToArray(arr.parent.gen(builder, arr.pin),
                val.parent.gen(builder, val.pin));

        Pin.Connector n = next.getConnector();
        String nextStr = next == null ? "" : n.parent.gen(builder, n.pin);

        return String.format("%s%s", add, nextStr);
    }
}
