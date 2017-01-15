package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.value.FloatValue;
import com.littleinferno.flowchart.value.Value;

public class FloatNode extends Node {

    private final TextField field;

    public FloatNode(Skin skin) {
        super("Float", true, skin);

        addDataOutputPin(Value.Type.FLOAT, "data");

        field = new TextField("", skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (c >= '0' && c <= '9' || c == '.')
                    return true;
                return false;
            }
        });
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public void eval() throws Exception {
        getPin("data").setValue(new FloatValue(Float.valueOf(field.getText())));
    }
}
