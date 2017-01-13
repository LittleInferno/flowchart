package com.littleinferno.flowchart.node;


import com.littleinferno.flowchart.Function;

public class FunctionCallNode extends Node {
    public FunctionCallNode(Function function) {
        super(function.getName());

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        function.addNode(this);
    }


    private Function function;

}
