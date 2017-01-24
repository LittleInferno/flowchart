package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;

public class FunctionCallNode extends Node {
    private Function function;
    private String currentCall;

    public FunctionCallNode(Function function, Skin skin) {
        super(function.getName(), true, skin);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        function.addNode(this);
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
