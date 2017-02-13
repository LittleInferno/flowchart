package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class StringNode extends Node {

    private final VisTextField field;

    public StringNode() {
        super("String", true);

        addDataOutputPin(DataType.STRING, "data");

        field = new VisTextField("");
        field.setWidth(100);
        ((Table) left.getParent().getParent()).add(field).growX();
        pack();
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        return String.format("\"%s\"", field.getText());
    }
}
