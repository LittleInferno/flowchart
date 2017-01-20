package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node {

    public LogNode(Skin skin) {
        super("Log", true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(Value.Type.STRING, "string");
    }

    @Override
    public String gen(Pin with) {
        Pin.Connector data = getPin("string").getConnector();
        String dataStr = data.parent.gen(data.pin);

        Pin.Connector next = getPin("exec out").getConnector();
        String nextStr = next == null ? "" : next.parent.gen(next.pin);

        return String.format("com.littleinferno.flowchart.codegen.IO.print(%s)\n%s", dataStr, nextStr);
    }
}
