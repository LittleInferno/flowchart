package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class IntegerNode extends Node {

    private final VisTextField field;

    public IntegerNode() {
        super("Integer", true);

        addDataOutputPin("data", DataType.INT);

        field = new VisTextField("");
        field.setTextFieldFilter((textField, c) -> c >= '0' && c <= '9' || c == '+' || c == '-');
        ((Table) left.getParent().getParent()).add(field).growX();
        pack();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeExpr(field.getText());
    }
}
