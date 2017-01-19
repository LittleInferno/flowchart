package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class IfNode extends Node implements CodeGen {
    public IfNode(Skin skin) {
        super("if", true, skin);

        addExecutionInputPin();
        addDataInputPin(Value.Type.BOOL, "Condition");

        addExecutionOutputPin("True");
        addExecutionOutputPin("False");
    }

    @Override
    public String gen() {

        CodeGen condition = (CodeGen) getPin("Condition").getConnectionNode();
        CodeGen tr = (CodeGen) getPin("True").getConnectionNode();

        CodeGen fl = (CodeGen) getPin("False").getConnectionNode();
        String el = "\n";
        if (fl != null) {
            el = String.format("else {\n%s\n}\n", fl.gen());
        }


        String result = String.format("if (%s) {\n%s}%s", condition.gen(), tr.gen(), el);

        return result;
    }
}
