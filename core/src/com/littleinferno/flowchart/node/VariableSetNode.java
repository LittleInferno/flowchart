package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.variable.Variable;


public class VariableSetNode extends Node {

    private Variable variable;

    private final Pin pin;
    private final Pin next;

    public VariableSetNode(Variable variable, Skin skin) {
        super(String.format("Set %s", variable.getName()), true);

        addExecutionInputPin();
        next = addExecutionOutputPin();

        this.pin = addDataInputPin(variable.getDataType(), "data");
        this.pin.setArray(variable.isArray());

        this.variable = variable;

        this.variable.addListener(this.pin::setArray);
        this.variable.addListener(this::setTitle);
        this.variable.addListener(this.pin::setType);
        this.variable.addListener(this::close);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        Pin.Connector data = pin.getConnector();
        String dataStr = data.parent.gen(builder, data.pin);

        Pin.Connector n = next.getConnector();
        String nextStr = n == null ? "" : n.parent.gen(builder, n.pin);

        return String.format("%s = %s\n%s", variable.getName(), dataStr, nextStr);
    }
}
