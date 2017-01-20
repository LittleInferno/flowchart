package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.Builder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class GreatNode extends Node {
    public GreatNode(Value.Type type, Skin skin) {
        super("great", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(Value.Type.BOOL, "A > B");
    }

    @Override
    public String gen(Pin with) {
        Pin.Connector a = getPin("A").getConnector();
        Pin.Connector b = getPin("B").getConnector();

        String aStr = a.parent.gen(a.pin);
        String bStr = b.parent.gen(b.pin);

        return Builder.createGt(aStr, bStr);
    }
}
