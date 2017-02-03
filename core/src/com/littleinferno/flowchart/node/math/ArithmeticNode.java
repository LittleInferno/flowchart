package com.littleinferno.flowchart.node.math;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.pin.PinListener;

public abstract class ArithmeticNode extends Node {

    protected final Pin a;
    protected final Pin b;
    private final Pin result;

    public ArithmeticNode(String name, DataType... possibleConvert) {
        super(name, true);

        a = addDataInputPin("A", possibleConvert);
        b = addDataInputPin("B", possibleConvert);
        result = addDataOutputPin("result", possibleConvert);

        PinListener defaultListener = new PinListener() {
            @Override
            public void typeChanged(DataType newType) {
                a.setType(newType);
                b.setType(newType);
                result.setType(newType);
            }
        };

        a.addListener(defaultListener);
        b.addListener(defaultListener);
        result.addListener(defaultListener);
    }
}
