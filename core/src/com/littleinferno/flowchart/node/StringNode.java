package com.littleinferno.flowchart.node;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.value.StringValue;
import com.littleinferno.flowchart.value.Value;

/**
 * Created by danil on 09.01.2017.
 */

public class StringNode extends Node {

    public StringNode(Vector2 position) {
        super(position, "String");

        addDataOutputPin(Value.Type.STRING, "data");

        field = new TextField("", skin);
        left.add(field);
    }

    @Override
    Value evaluate() {
        return new StringValue(field.getText());
    }

    private final TextField field;
}
