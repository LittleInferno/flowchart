package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.pin.Pin;

public class VariableGetNode extends Node {

    private final Variable variable;

    public VariableGetNode(Variable variable, Skin skin) {
        super(String.format("Get %s", variable.getName()), true, skin);

        this.variable = variable;
        variable.addNode(this);

        addDataOutputPin(variable.getValueType(), "data");
    }

    @Override
    public String gen(Pin with) {
        return variable.getName();
    }
}
