package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.VariableChangedListener;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

public class VariableGetNode extends Node {

    private final Variable variable;

    public VariableGetNode(Variable variable, Skin skin) {
        super(String.format("Get %s", variable.getName()), true, skin);

        final Pin pin = addDataOutputPin(variable.getDataType(), "data");
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
        return variable.getName();
    }
}
