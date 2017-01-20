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
    public String gen(Pin with) {

        Pin.Connector condition = getPin("Condition").getConnector();

        Pin.Connector tr = getPin("True").getConnector();
        Pin.Connector fl = getPin("False").getConnector();

        String conditionStr = condition.parent.gen(condition.pin);
        String trueString = tr.parent.gen(tr.pin);
        String falseString = fl == null ? "" : fl.parent.gen(fl.pin);

        return String.format("if (%s) {\n%s}%s", conditionStr, trueString, falseString);
    }
}
