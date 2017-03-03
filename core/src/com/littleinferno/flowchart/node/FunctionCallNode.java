package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallNode extends FunctionNode {

    private final Pin next;
    private String currentCall;

    private List<Pin> pins;
    private int outParametrsSize;

    public FunctionCallNode(Function function) {
        super(function, function.getName(), true);

        addExecutionInputPin("exec in");
        next = addExecutionOutputPin("exec out");

        this.function = function;
        this.function.addListener(this::setTitle);
        this.function.addListener(this::close);

        pins = new ArrayList<>();


        this.function.addPareameterListener(
                parameter -> {
                    final Pin pin;
                    if (parameter.getConnection() == Connection.INPUT)
                        pin = addDataInputPin(parameter.getName(), parameter.getDataType());
                    else
                        pin = addDataOutputPin(parameter.getName(), parameter.getDataType());

                    pin.setArray(parameter.isArray());

                    parameter.addListener(pin::setArray);
                    parameter.addListener(pin::setName);
                    parameter.addListener(pin::setType);

                    pins.add(pin);
                },
                parameter -> {
                    for (Pin pin : pins)
                        if (parameter.getName().equals(pin.getName()))
                            removePin(pin);
                });

        this.function.applyParameters();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        if (with.getType() == DataType.EXECUTION) {

            ArrayList<String> params = new ArrayList<>();

            final int[] inParametrsSize = {0};
            Stream.of(pins)
                    .filter(i -> i.getType() != DataType.EXECUTION &&
                            i.getConnection() == Connection.INPUT)
                    .forEach(i -> {
                        Pin.Connector data = i.getConnector();
                        params.add(data.parent.gen(builder, data.pin));
                        ++inParametrsSize[0];
                    });

            outParametrsSize = pins.size() - inParametrsSize[0];

            currentCall = builder.makeNamedValue("tmpcall");

            Pin.Connector n = next.getConnector();
            String nextStr = n == null ? "" : n.parent.gen(builder, n.pin);


            return String.format("%s%s",
                    builder.makeCall(function.getName(), params, currentCall), nextStr);
        }

        if (outParametrsSize == 1)
            return currentCall;

        return String.format("%s.%s", currentCall, with.getName());
    }

    @Override
    public void close() {
        super.close();
    }
}
