package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.Builder;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class AddNode extends Node implements CodeGen {
    public AddNode(Value.Type type, Skin skin) {
        super("add", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(type, "A + B");
    }

    @Override
    public String gen() {
        CodeGen a = (CodeGen) getPin("A").getConnectionNode();
        CodeGen b = (CodeGen) getPin("B").getConnectionNode();

        return Builder.createAdd(a.gen(), b.gen());
    }
}
