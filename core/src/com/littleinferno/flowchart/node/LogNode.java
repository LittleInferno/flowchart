package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class LogNode extends Node {

    public LogNode(Skin skin) {
        super("Log", true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(DataType.STRING, "string");
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector data = getPin("string").getConnector();
        String dataStr = data.parent.gen(builder, data.pin);

        Pin.Connector next = getPin("exec out").getConnector();
        String nextStr = next == null ? "" : next.parent.gen(builder, next.pin);

        return String.format("com.littleinferno.flowchart.codegen.IO.print(%s)\n%s", dataStr, nextStr);
    }
}
