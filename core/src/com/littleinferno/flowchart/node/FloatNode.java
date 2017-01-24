package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class FloatNode extends Node {

    private final TextField field;

    public FloatNode(Skin skin) {
        super("Float", true, skin);

        addDataOutputPin(DataType.FLOAT, "data");

        field = new TextField("", skin);
        field.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return c >= '0' && c <= '9' || c == '.' || c == '+' || c == '-';
            }
        });
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        return field.getText();
    }
}
