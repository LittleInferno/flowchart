package com.littleinferno.flowchart.node;


import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Function;

public class FunctionBeginNode extends Node {
    public FunctionBeginNode(Vector2 position, Function function) {
        super(position, function.getName());

        addExecutionOutputPin("exec out");
    }

}
