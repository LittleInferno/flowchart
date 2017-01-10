package com.littleinferno.flowchart.node;

import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.value.Value;

/**
 * Created by danil on 09.01.2017.
 */

public class ConverterNode extends Node {
    public ConverterNode(Vector2 position, Value.Type from, Value.Type to) {
        super(position, "Converter");
        this.to = to;

        addDataInputPin(from, "from");
        addDataOutputPin(to, "to");
    }


    @Override
    Value evaluate() {
        Node node = get("from").getConnectionNode();

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
