package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.codegen.CodeGen;


public class VariableSetNode extends Node implements CodeGen {

    private Variable variable;

    public VariableSetNode(Variable variable, Skin skin) {
        super(String.format("Set %s", variable.getName()), true, skin);

        this.variable = variable;
        variable.addNode(this);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(variable.getValueType(), "data");
    }

    @Override
    public String gen() {
        CodeGen data = (CodeGen) getPin("data").getConnectionNode();
        CodeGen next = (CodeGen) getPin("exec out").getConnectionNode();

        return String.format("%s = %s\n%s", variable.getName(), data.gen(),
                next != null ? next.gen() : "");
    }
}
