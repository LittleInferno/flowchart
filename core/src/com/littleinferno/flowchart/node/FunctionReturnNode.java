package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionReturnNode extends Node {

    private Function function;

    private List<Pin> pins;

    public FunctionReturnNode(Function function) {
        super(function.getName(), false);

        addExecutionInputPin();

        this.function = function;
        this.function.addListener(this::setTitle);
        this.function.addListener(this::close);

        pins = new ArrayList<>();

        function.addPareameterListener(
                parameter -> {
                    if (parameter.getConnection() == Connection.OUTPUT) {
                        final Pin pin = addDataInputPin(parameter.getDataType(), parameter.getName());

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
    public String gen(CodeBuilder builder, Pin with) {
        Map<String, String> returnPack = new HashMap<>();

        Stream.of(pins)
                .filter(i -> i.getType() != DataType.EXECUTION)
                .forEach(i -> {
                    Pin.Connector node = i.getConnector();

                    String p = node.parent.gen(builder, node.pin);
                    returnPack.put(i.getName(), p);
                });

        return builder.createReturn(returnPack);
    }

    @Override
    public void close() {
        super.close();
        function.removeReturnNode(this);
    }
}
