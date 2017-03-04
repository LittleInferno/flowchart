package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class StringNode extends Node {

    private VisTextField field;

    public StringNode() {
        this(new NodeHandle("String", true));
    }

    public StringNode(NodeHandle nodeHandle) {
        super(nodeHandle);

        addDataOutputPin("data", DataType.STRING);

        field = new VisTextField("");
        field.setWidth(100);
        ((Table) left.getParent().getParent()).add(field).growX();
        pack();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeString(field.getText());
    }
}
