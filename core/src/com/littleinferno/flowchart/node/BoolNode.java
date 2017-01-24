package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class BoolNode extends Node {
    private final SelectBox box;

    public BoolNode(Skin skin) {
        super("Bool", true, skin);

        addDataOutputPin(DataType.BOOL, "data");

        box = new SelectBox(skin);
        box.setItems("True", "False");
        left.add(box).expandX().fillX().minWidth(0);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        return box.getSelectedIndex() == 0 ? "true" : "false";
    }
}
