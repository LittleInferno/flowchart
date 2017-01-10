package com.littleinferno.flowchart.node;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.IntegerValue;
import com.littleinferno.flowchart.value.Value;

public class IntegerNode extends Node {

    public IntegerNode(Vector2 position) {
        super(position, "Integer");

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
        left.add(field);
    }

    @Override
    Value evaluate() {
        return new IntegerValue(Integer.valueOf(field.getText()));
    }

    private final TextField field;
}
