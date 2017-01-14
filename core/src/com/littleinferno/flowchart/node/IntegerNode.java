package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.value.IntegerValue;
import com.littleinferno.flowchart.value.Value;

public class IntegerNode extends Node {

    private final TextField field;

    public IntegerNode() {
        super("Integer", true);

        addDataOutputPin(Value.Type.INT, "data");

        field = new TextField("", skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                if (c >= '0' && c <= '9')
                    return true;
                return false;
            }
        });
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public void eval() throws Exception {
        getPin("data").setValue(new IntegerValue(Integer.valueOf(field.getText())));
    }
}
