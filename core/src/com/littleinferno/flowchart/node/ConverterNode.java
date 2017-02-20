package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class ConverterNode extends Node {
    public ConverterNode(DataType from, DataType to, Skin skin) {
        super("Converter", true);

        addDataInputPin("from", from);
        addDataOutputPin("to", to);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
      //  Pin.Connector from = getPin("from").getConnector();

      //  return from.parent.gen(builder, from.pin);
        return null;
    }
}
