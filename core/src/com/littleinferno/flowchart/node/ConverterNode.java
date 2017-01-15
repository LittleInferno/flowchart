package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.value.Value;

public class ConverterNode extends Node {
    public ConverterNode(Value.Type from, Value.Type to, Skin skin) {
        super("Converter", true, skin);

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }

    @Override
    public void eval() throws Exception {
        getPin("to").setValue(
                getPin("from").getConnectionPin().getValue());
    }
}
