package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node implements CodeGen {

    public LogNode(Skin skin) {
        super("Log", true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(Value.Type.STRING, "string");
    }

    @Override
    public String gen() {

        CodeGen string = (CodeGen) getPin("string").getConnectionNode();
        CodeGen next = (CodeGen) getPin("exec out").getConnectionNode();

        return String.format("com.littleinferno.flowchart.codegen.IO.print(%s)\n%s", string.gen(),
                next != null ? next.gen() : "");
    }
}
