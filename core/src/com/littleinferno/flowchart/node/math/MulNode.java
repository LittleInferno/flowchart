package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class MulNode extends Node {

    public MulNode(Value.Type type, Skin skin) {
        super("mul", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(type, "A * B");
    }

    @Override
    public void eval() throws Exception {
        Value a = getPin("A").getConnectionPin().getValue();
        Value b = getPin("B").getConnectionPin().getValue();

        getPin("A * B").setValue(Value.mul(a, b));
    }
}
