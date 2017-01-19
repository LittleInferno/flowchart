package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.IntegerValue;
import com.littleinferno.flowchart.value.Value;

public class IntegerNode extends Node implements CodeGen {

    private final TextField field;

    public IntegerNode(Skin skin) {
        super("Integer", true, skin);

        addDataOutputPin(Value.Type.INT, "data");

        field = new TextField("", skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return c >= '0' && c <= '9' || c == '+' || c == '-';
            }
        });
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public String gen() {
        return field.getText();
    }
}
