package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionCallNode extends Node {
    private Function function;

    public FunctionCallNode(Function function, Skin skin) {
        super(function.getName(), true, skin);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        function.addNode(this);
    }

    @Override
    public void execute() {
        function.setCurrentCall(this);

        Array<Pin> inputs = getInput();
        FunctionBeginNode begNode = function.getBeginNode();
        for (Pin i : inputs) {
            if (i.getType() != Value.Type.EXECUTION)
                begNode.getPin(i.getName()).setValue(i.getConnectionPin().getValue());
        }

        begNode.execute();

        executeNext();
    }

    @Override
    public void eval() {
    }
}
