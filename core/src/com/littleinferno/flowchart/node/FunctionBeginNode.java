package com.littleinferno.flowchart.node;


import com.littleinferno.flowchart.Function;

public class FunctionBeginNode extends Node {
    public FunctionBeginNode(Function function) {
        super(function.getName(), false);

        addExecutionOutputPin();
    }

    @Override
    public void eval() {
    }

    @Override
    public void execute() {
        executeNext();
    }
}
