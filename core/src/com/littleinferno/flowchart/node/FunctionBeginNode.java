package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.CodeGen;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionBeginNode extends Node implements CodeGen {

    private final Function function;

    public FunctionBeginNode(Function function, Skin skin) {
        super(function.getName(), false, skin);
        this.function = function;

        addExecutionOutputPin();
    }

    @Override
    public String gen() {

        Array<Pin> output = getOutput();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < output.size; i++) {
            if (output.get(i).getType() != Value.Type.EXECUTION)
                builder.append(output.get(i).getName());

            if (i != output.size - 1) {
                builder.append(',');
            }
        }

        CodeGen string = (CodeGen) getPin("exec out").getConnectionNode();

        return String.format("function %s(%s){\n%s\n}", function.getName(), builder.toString(), string.gen());
    }
}
