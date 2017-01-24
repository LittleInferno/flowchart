package com.littleinferno.flowchart.node.array;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class ArrayGetNode extends Node {

    private Pin array;
    private Pin item;


    public ArrayGetNode(DataType type, Skin skin) {
        super("Get", true, skin);

        array = addDataInputPin(type, "array");
        array.setArray(true);
        item = addDataInputPin(DataType.INT, "index");
        addDataOutputPin(type, "item");
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        Pin.Connector a = array.getConnector();
        Pin.Connector i = item.getConnector();
        return builder.createGetArrayItem(a.parent.gen(builder, a.pin),
                i.parent.gen(builder, i.pin));
    }
}
