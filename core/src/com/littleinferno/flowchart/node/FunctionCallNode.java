package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.NameChangeable;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.parameter.Parameter;
import com.littleinferno.flowchart.parameter.ParameterChangedListener;
import com.littleinferno.flowchart.parameter.ParameterListener;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallNode extends Node {
    private Function function;
    private String currentCall;

    public FunctionCallNode(Function function, Skin skin) {
        super(function.getName(), true, skin);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        this.function.addListener(new NameChangeable.NameChange() {
            @Override
            public void changed(String newName) {
                setTitle(newName);
            }
        });

        this.function.addListener(new ParameterListener() {
            private List<Pin> pins = new ArrayList<Pin>();

            @Override
            public void parameterAdded(Parameter parameter) {
                final Pin pin;
                if (parameter.getConnection() == Connection.INPUT)
                    pin = addDataInputPin(parameter.getDataType(), parameter.getName());
                else
                    pin = addDataOutputPin(parameter.getDataType(), parameter.getName());

                pin.setArray(parameter.isArray());

                parameter.addListener(new ParameterChangedListener() {
                    @Override
                    public void nameChanged(String newName) {
                        pin.setName(newName);
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
                pins.add(pin);
            }

            @Override
            public void parameterRemoved(Parameter parameter) {
                for (Pin pin : pins)
                    if (parameter.getName().equals(pin.getName()))
                        removePin(pin);
            }
        });


        this.function.addNode(this);
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        if (with.getType() == DataType.EXECUTION) {
            Array<Pin> inputs = getInput();

            ArrayList<String> params = new ArrayList<String>();

            for (Pin i : inputs) {
                Pin.Connector data = i.getConnector();
                if (i.getType() != DataType.EXECUTION)
                    params.add(data.parent.gen(builder, data.pin));
            }

            currentCall = builder.createNamedValue("tmpcall");

            Pin.Connector next = getPin("exec out").getConnector();
            String nextStr = next == null ? "" : next.parent.gen(builder, next.pin);

            return String.format("%s%s",
                    builder.createCall(function.getName(), params, currentCall), nextStr);
        }

        return String.format("%s.%s", currentCall, with.getName());
    }
}
