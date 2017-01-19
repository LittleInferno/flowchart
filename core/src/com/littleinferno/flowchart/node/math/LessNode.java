package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.Builder;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class LessNode extends Node implements CodeGen {
    public LessNode(Value.Type type, Skin skin) {
        super("less", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(Value.Type.BOOL, "A < B");
    }

    @Override
    public String gen() {
        CodeGen a = (CodeGen) getPin("A").getConnectionNode();
        CodeGen b = (CodeGen) getPin("B").getConnectionNode();

        return Builder.createLt(a.gen(), b.gen());
    }
}