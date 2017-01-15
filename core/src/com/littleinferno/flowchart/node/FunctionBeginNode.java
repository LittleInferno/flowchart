package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.Function;

public class FunctionBeginNode extends Node {
    public FunctionBeginNode(Function function, Skin skin) {
        super(function.getName(), false, skin);

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
