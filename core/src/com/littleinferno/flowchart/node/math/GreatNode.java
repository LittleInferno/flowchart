package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.Builder;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class GreatNode extends Node implements CodeGen {
    public GreatNode(Value.Type type, Skin skin) {
        super("great", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(Value.Type.BOOL, "A > B");
    }

    @Override
    public String gen() {
        CodeGen a = (CodeGen) getPin("A").getConnectionNode();
        CodeGen b = (CodeGen) getPin("B").getConnectionNode();

        return Builder.createGt(a.gen(), b.gen());
    }
}
