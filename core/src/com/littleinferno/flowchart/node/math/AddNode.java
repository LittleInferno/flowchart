package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.Builder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class AddNode extends Node {
    public AddNode(Value.Type type, Skin skin) {
        super("add", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(type, "A + B");
    }

    @Override
    public String gen(Pin with) {
        Pin.Connector a = getPin("A").getConnector();
        Pin.Connector b = getPin("B").getConnector();

        String aStr = a.parent.gen(a.pin);
        String bStr = b.parent.gen(b.pin);

        return Builder.createAdd(aStr, bStr);
    }
}
