package com.littleinferno.flowchart.node;


import com.littleinferno.flowchart.Function;

public class FunctionBeginNode extends Node {
    public FunctionBeginNode(Function function) {
        super(function.getName());

        addExecutionOutputPin("exec out");
    }

}
