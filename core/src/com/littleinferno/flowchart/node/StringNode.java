package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.value.StringValue;
import com.littleinferno.flowchart.value.Value;

public class StringNode extends Node {

    public StringNode() {
        super("String");

        addDataOutputPin(Value.Type.STRING, "data");

        field = new TextField("", skin);
        left.add(field).expandX().fillX().minWidth(0);;
    }

    @Override
    Value evaluate() {
        return new StringValue(field.getText());
    }

    private final TextField field;
}
