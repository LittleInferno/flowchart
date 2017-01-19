package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.codegen.CodeGen;

public class VariableGetNode extends Node implements CodeGen {

    public VariableGetNode(Variable variable, Skin skin) {
        super(String.format("Get %s", variable.getName()), true, skin);

        this.variable = variable;
        variable.addNode(this);

        addDataOutputPin(variable.getValueType(), "data");
    }

    private Variable variable;

    @Override
    public String gen() {
        return variable.getName();
    }
}
