package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class IfNode extends Node {
    public IfNode(Skin skin) {
        super("if", true, skin);

        addExecutionInputPin();
        addDataInputPin(Value.Type.BOOL, "Condition");

        addExecutionOutputPin("True");
        addExecutionOutputPin("False");
    }

    @Override
    public void execute() throws Exception {
        Pin a = getPin("Condition").getConnectionPin();

        if (a != null) {

            Node next;

            if (a.getValue().asBool())
                next = getPin("True").getConnectionNode();
            else
                next = getPin("False").getConnectionNode();

            if (next != null)
                next.execute();
        }
    }
}
