package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;

public class FunctionBeginNode extends Node {

    private final Function function;

    public FunctionBeginNode(Function function, Skin skin) {
        super(function.getName(), false, skin);
        this.function = function;

        addExecutionOutputPin();
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        if (with.getType() == DataType.EXECUTION) {

            Array<Pin> output = getOutput();

            ArrayList<String> params = new ArrayList<String>();

            for (Pin i : output) {
                if (i.getType() != DataType.EXECUTION) params.add(i.getName());
            }

            String parameters = builder.createParams(params);

            Pin.Connector next = getPin("exec out").getConnector();
            String nextStr = next == null ? "" : next.parent.gen(builder, next.pin);

            return builder.createFunction(function.getName(), parameters, nextStr);
        }

        return with.getName();
    }
}
