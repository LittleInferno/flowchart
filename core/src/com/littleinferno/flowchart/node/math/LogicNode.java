package com.littleinferno.flowchart.node.math;


import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

public abstract class LogicNode extends Node {

    protected final Pin a;
    protected final Pin b;

    public LogicNode(String name, DataType... possibleConvert) {
        super(name, true);

        a = addDataInputPin("A", possibleConvert);
        b = addDataInputPin("B", possibleConvert);
        addDataOutputPin(DataType.BOOL, "result");

        Pin.PinListener listener = t -> {
            a.setType(t);
            b.setType(t);
        };

        a.addListener(listener);
        b.addListener(listener);
    }
}
