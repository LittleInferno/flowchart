package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.value.Value;

public class ConverterNode extends Node {
    public ConverterNode(Value.Type from, Value.Type to) {
        super("Converter", true);
        this.to = to;

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }


    @Override
    Value evaluate() {
        Node node = getItem("from").getPin().getConnectionNode();

        if (node != null) {

            Value value = null;
            try {
                value = node.evaluate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }

        return null;
    }


    private Value.Type to;
}
