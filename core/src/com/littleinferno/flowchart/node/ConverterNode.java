package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.value.Value;

public class ConverterNode extends Node {
    public ConverterNode(Value.Type from, Value.Type to) {
        super("Converter", true);

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }

    @Override
    public void eval() throws Exception {
        getPin("to").setValue(
                getPin("from").getConnectionPin().getValue());
    }
}
