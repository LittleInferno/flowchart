package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class EqualsNode extends Node {
    public EqualsNode(Value.Type type, Skin skin) {
        super("equals", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(Value.Type.BOOL, "A == B");
    }

    @Override
    public void eval() throws Exception {
        Value a = getPin("A").getConnectionPin().getValue();
        Value b = getPin("B").getConnectionPin().getValue();

        getPin("A == B").setValue(Value.equals(a, b));
    }
}
