package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

public class FunctionCallNode extends Node {
    private Function function;
    private String currentTmpCall;
    private static int counter = 0;

    public FunctionCallNode(Function function, Skin skin) {
        super(function.getName(), true, skin);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        function.addNode(this);
    }

    @Override
    public String gen(Pin with) {

        if (with.getType() == Value.Type.EXECUTION) {
            Array<Pin> inputs = getInput();
            StringBuilder parametrBuilder = new StringBuilder();

            for (int i = 0; i < inputs.size; i++) {
                if (inputs.get(i).getType() != Value.Type.EXECUTION) {

                    Pin.Connector data = inputs.get(i).getConnector();
                    parametrBuilder.append(data.parent.gen(data.pin));

                    if (i != inputs.size - 1)
                        parametrBuilder.append(',');
                }
            }

            currentTmpCall = String.format("tmpcall%d", counter++);

            Pin.Connector next = getPin("exec out").getConnector();
            String nextStr = next == null ? "" : next.parent.gen(next.pin);

            return String.format("var %s = %s(%s)\n%s", currentTmpCall, function.getName(),
                    parametrBuilder.toString(), nextStr);

        }

        return String.format("%s.%s", currentTmpCall, with.getName());
    }
}
