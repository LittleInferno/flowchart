package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class ConverterNode extends Node {
    public ConverterNode(Value.Type from, Value.Type to, Skin skin) {
        super("Converter", true, skin);

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }

    @Override
    public String gen(Pin with) {
        Pin.Connector from = getPin("from").getConnector();

        return from.parent.gen(from.pin);
    }
}
