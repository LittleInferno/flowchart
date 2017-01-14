package com.littleinferno.flowchart.node;

import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionReturnNode extends Node {
    private Function function;

    public FunctionReturnNode(Function function) {
        super(function.getName(), false);
        this.function = function;

        addExecutionInputPin();
    }

    @Override
    public void execute() {

        Array<Pin> inputs = getInput();
        FunctionCallNode node = function.getCurrentCall();
        for (Pin i : inputs) {
            if (i.getType() != Value.Type.EXECUTION) {
                node.getPin(i.getName()).setValue(i.getConnectionPin().getValue());
            }
        }
    }
}
