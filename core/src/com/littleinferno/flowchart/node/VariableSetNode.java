package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.VariableChangedListener;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;


public class VariableSetNode extends Node {

    private Variable variable;

    public VariableSetNode(Variable variable, Skin skin) {
        super(String.format("Set %s", variable.getName()), true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        final Pin pin = addDataInputPin(variable.getDataType(), "data");
        pin.setArray(variable.isArray());

        this.variable = variable;
        this.variable.addListener(new VariableChangedListener() {
            @Override
            public void nameChanged(String newName) {
                setTitle(newName);
            }

            @Override
            public void typeChanged(DataType newType) {
                pin.setType(newType);
            }

            @Override
            public void isArrayChanged(boolean isArray) {
                pin.setArray(isArray);
            }
        });

        this.variable.addNode(this);

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
