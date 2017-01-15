package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Variable;

public class VariableGetNode extends Node {

    public VariableGetNode(Variable variable, Skin skin) {
        super(String.format("Get %s", variable.getName()), true, skin);

        this.variable = variable;
        variable.addNode(this);

        addDataOutputPin(variable.getValueType(), "data");
    }

    @Override
    public void eval() throws Exception {
        getPin("data").setValue(variable.getValue());
    }

    private Variable variable;
}
