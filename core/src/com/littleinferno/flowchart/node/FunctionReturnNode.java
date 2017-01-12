package com.littleinferno.flowchart.node;

import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Function;

public class FunctionReturnNode extends Node {
    public FunctionReturnNode(Vector2 position, Function function) {
        super(position, function.getName());

        addExecutionInputPin("exec in");
    }
}
