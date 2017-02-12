package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

abstract class ArithmeticNode extends Node {

    protected final Pin a;
    protected final Pin b;

    ArithmeticNode(String name, DataType... possibleConvert) {
        super(name, true);

        a = addDataInputPin("A", possibleConvert);
        b = addDataInputPin("B", possibleConvert);
        Pin result = addDataOutputPin("result", possibleConvert);

        Pin.PinListener listener = t -> {
            a.setType(t);
            b.setType(t);
            result.setType(t);
        };

        a.addListener(listener);
        b.addListener(listener);
        result.addListener(listener);
    }
}
