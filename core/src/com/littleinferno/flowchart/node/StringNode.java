package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class StringNode extends Node {

    private final TextField field;

    public StringNode(Skin skin) {
        super("String", true, skin);

        addDataOutputPin(DataType.STRING, "data");

        field = new TextField("", skin);
        left.add(field).expandX().fillX().minWidth(0);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with)  {
        return String.format("\"%s\"", field.getText());
    }
}
