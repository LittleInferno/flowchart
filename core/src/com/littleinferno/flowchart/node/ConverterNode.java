package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.value.Value;

public class ConverterNode extends Node implements CodeGen {
    public ConverterNode(Value.Type from, Value.Type to, Skin skin) {
        super("Converter", true, skin);

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }

    @Override
    public String gen() {
        return ((CodeGen) getPin("from").getConnectionNode()).gen();
    }
}
