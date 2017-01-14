package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.value.StringValue;
import com.littleinferno.flowchart.value.Value;

public class StringNode extends Node {

    private final TextField field;

    public StringNode() {
        super("String", true);

        addDataOutputPin(Value.Type.STRING, "data");

        field = new TextField("", skin);
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public void eval() throws Exception {
        getPin("data").setValue(new StringValue(field.getText()));
    }
}
