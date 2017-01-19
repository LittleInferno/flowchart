package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionCallNode extends Node implements CodeGen {
    private Function function;

    public FunctionCallNode(Function function, Skin skin) {
        super(function.getName(), true, skin);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        function.addNode(this);
    }

    @Override
    public String gen() {
        Array<Pin> inputs = getInput();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < inputs.size; i++) {
            if (inputs.get(i).getType() != Value.Type.EXECUTION)
                builder.append(inputs.get(i).getName());

            if (i != inputs.size - 1) {
                builder.append(',');
            }
        }

        return String.format("%s(%s)", function.getName(), builder.toString());
    }
}
