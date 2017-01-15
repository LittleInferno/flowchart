package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Variable;


public class VariableSetNode extends Node {

    public VariableSetNode(Variable variable, Skin skin) {
        super(String.format("Set %s", variable.getName()), true, skin);

        this.variable = variable;
        variable.addNode(this);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(variable.getValueType(), "data");
    }


    @Override
    public void execute() {

        variable.setValue(getPin("data").getConnectionPin().getValue());

        executeNext();
    }

    private Variable variable;
}
