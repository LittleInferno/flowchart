package com.littleinferno.flowchart.node;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class FunctionReturnNode extends Node {

    private Function function;

    private List<Pin> pins;

    public FunctionReturnNode(Function function) {
        super(new NodeHandle(function.getName(), false));

        addExecutionInputPin();

        this.function = function;
        this.function.addListener(this::setTitle);
        this.function.addListener(this::close);

        pins = new ArrayList<>();

        function.addPareameterListener(
                parameter -> {
                    if (parameter.getConnection() == Connection.OUTPUT) {
                        final Pin pin = addDataInputPin(parameter.getName(), parameter.getDataType());

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

        this.function.addReturnNode(this);

    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        if (pins.isEmpty())
            return BaseCodeGenerator.EMPTY_EXPRESSION;

        if (pins.size() == 1)
            return builder.makeReturn(pins.get(0));


        return builder.makeReturn(
                Stream.of(pins)
                        .filter(i -> i.getType() != DataType.EXECUTION)
                        .collect(Collectors.toList()));
    }

    @Override
    public void close() {
        super.close();
        function.removeReturnNode(this);
    }
}
