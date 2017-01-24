package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.pin.Pin;

import java.util.HashMap;
import java.util.Map;

public class FunctionReturnNode extends Node {

    public FunctionReturnNode(Function function, Skin skin) {
        super(function.getName(), false, skin);
        addExecutionInputPin();
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Array<Pin> input = getInput();

        Map<String, String> returnPack = new HashMap<String, String>();

        for (Pin i : input) {

            if (i.getType() != DataType.EXECUTION) {
                Pin.Connector node = i.getConnector();

                String p = node.parent.gen(builder, node.pin);
                returnPack.put(i.getName(), p);
            }
        }

        return builder.createReturn(returnPack);
    }
}
