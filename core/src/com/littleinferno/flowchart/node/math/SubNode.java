package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public class SubNode extends Node {
    public SubNode(DataType type, Skin skin) {
        super("sub", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(type, "A - B");
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector a = getPin("A").getConnector();
        Pin.Connector b = getPin("B").getConnector();

        String aStr = a.parent.gen(builder, a.pin);
        String bStr = b.parent.gen(builder, b.pin);

        return builder.createSub(aStr, bStr);
    }
}
