package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;


public class VariableSetNode extends Node {

    private Variable variable;

    public VariableSetNode(Variable variable, Skin skin) {
        super(String.format("Set %s", variable.getName()), true, skin);

        this.variable = variable;
        variable.addNode(this);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(variable.getValueType(), "data");
        getPin("data").setArray(variable.isArray());
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector data = getPin("data").getConnector();
        String dataStr = data.parent.gen(builder, data.pin);

        Pin.Connector next = getPin("exec out").getConnector();
        String nextStr = next == null ? "" : next.parent.gen(builder, next.pin);

        return String.format("%s = %s\n%s", variable.getName(), dataStr, nextStr);
    }
}
