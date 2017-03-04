package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class FloatNode extends Node {

    private VisTextField field;

    public FloatNode() {
        this(new NodeHandle("Float", true));
    }

    public FloatNode(NodeHandle nodeHandle) {
        super(nodeHandle);

        addDataOutputPin("data", DataType.FLOAT);

        field = new VisTextField("");
        field.setTextFieldFilter((textField, c) ->
                c >= '0' && c <= '9' || c == '.' || c == '+' || c == '-');
        ((Table) left.getParent().getParent()).add(field).growX();
        pack();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return field.getText();
    }
}
