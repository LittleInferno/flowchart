package com.littleinferno.flowchart.node;


import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class FunctionBeginNode extends Node {

    private final Function function;

    private final Pin next;

    private List<Pin> pins;

    public FunctionBeginNode(Function function) {
        super(function.getName(), false);

        next = addExecutionOutputPin();

        this.function = function;
        this.function.addListener(this::setTitle);
        this.function.addListener(this::close);
        this.function.setGenerateListener(builder -> gen(builder, next));
        pins = new ArrayList<>();

        function.addPareameterListener(
                parameter -> {
                    if (parameter.getConnection() == Connection.INPUT) {
                        final Pin pin = addDataOutputPin(parameter.getDataType(), parameter.getName());

                        pin.setArray(parameter.isArray());

                        parameter.addListener(pin::setArray);
                        parameter.addListener(pin::setName);
                        parameter.addListener(pin::setType);

                        pins.add(pin);
                    }
                },
                parameter -> {
                    for (Pin pin : pins)
                        if (parameter.getName().equals(pin.getName()))
                            removePin(pin);
                }
        );

        this.function.applyParameters();

    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        if (with.getType() == DataType.EXECUTION) {

            ArrayList<String> params = new ArrayList<>();

            Stream.of(pins)
                    .filter(i -> i.getType() != DataType.EXECUTION)
                    .forEach(i -> params.add(i.getName()));

            String parameters = builder.createParams(params);

            Pin.Connector n = next.getConnector();
            String nextStr = n == null ? "" : n.parent.gen(builder, n.pin);

            return builder.createFunction(function.getName(), parameters, nextStr);
        }

        return with.getName();
    }
}
