package com.littleinferno.flowchart.node;


import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
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
    public String gen(BaseCodeGenerator builder, Pin with) {

        if (with.getType() == DataType.EXECUTION) {

            ArrayList<String> params = new ArrayList<>();

            Stream.of(pins)
                    .filter(i -> i.getType() != DataType.EXECUTION)
                    .forEach(i -> params.add(i.getName()));

            String parameters = builder.makeParams(params);

            Pin.Connector n = next.getConnector();
            String nextStr = n == null ? "" : n.parent.gen(builder, n.pin);

            return builder.makeFunction(function.getName(), parameters, builder.makeBlock(nextStr));
        }

        return with.getName();
    }
}
