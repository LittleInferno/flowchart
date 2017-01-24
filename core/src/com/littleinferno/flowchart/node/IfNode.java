package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class IfNode extends Node {
    public IfNode(Skin skin) {
        super("if", true, skin);

        addExecutionInputPin();
        addDataInputPin(DataType.BOOL, "Condition");

        addExecutionOutputPin("True");
        addExecutionOutputPin("False");
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        Pin.Connector condition = getPin("Condition").getConnector();

        Pin.Connector tr = getPin("True").getConnector();
        Pin.Connector fl = getPin("False").getConnector();

        String conditionStr = condition.parent.gen(builder, condition.pin);
        String trueString = tr.parent.gen(builder, tr.pin);
        String falseString = fl == null ? "" : fl.parent.gen(builder, fl.pin);

        return String.format("if (%s) {\n%s}%s", conditionStr, trueString, falseString);
    }
}
