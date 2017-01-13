package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.Function;

public class FunctionReturnNode extends Node {
    public FunctionReturnNode(Function function) {
        super(function.getName());

        addExecutionInputPin("exec in");
    }
}
