package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionReturnNode extends Node {

    public FunctionReturnNode(Function function, Skin skin) {
        super(function.getName(), false, skin);
        addExecutionInputPin();
    }

    @Override
    public String gen(Pin with) {
        Array<Pin> input = getInput();
        StringBuilder builder = new StringBuilder();
        StringBuilder returnPack = new StringBuilder();
        returnPack.append("return {");

        for (int i = 0; i < input.size; i++) {
            Pin pin = input.get(i);

            if (pin.getType() != Value.Type.EXECUTION) {
                builder.append("var ").
                        append(pin.getName()).
                        append(" = ");

                Pin.Connector node = pin.getConnector();

                builder.append(node.parent.gen(node.pin)).append("\n");

                returnPack.append(pin.getName()).append(':').append(pin.getName());
                if (i != input.size - 1) {
                    returnPack.append(',');
                }
            }
        }
        returnPack.append("}\n");

        return builder.toString() + returnPack.toString();
    }
}
