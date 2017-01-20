package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class BoolNode extends Node {
    private final SelectBox box;

    public BoolNode(Skin skin) {
        super("Bool", true, skin);

        addDataOutputPin(Value.Type.BOOL, "data");

        box = new SelectBox(skin);
        box.setItems("True", "False");
        left.add(box).expandX().fillX().minWidth(0);
    }

    @Override
    public String gen(Pin with) {
        return box.getSelectedIndex() == 0 ? "true" : "false";
    }
}
