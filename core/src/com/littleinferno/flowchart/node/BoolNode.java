package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class BoolNode extends Node {
    private final VisSelectBox<String> box;

    public BoolNode() {
        super("Bool", true);

        addDataOutputPin("data", DataType.BOOL);

        box = new VisSelectBox<>();
        box.setItems("True", "False");
        ((Table) left.getParent().getParent()).add(box).growX();
        pack();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return box.getSelectedIndex() == 0 ? "true" : "false";
    }
}
