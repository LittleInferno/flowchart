package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node {


    public LogNode(Skin skin) {
        super("Log", true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(Value.Type.STRING, "string");
    }

    @Override
    public void execute() {

        Value str = getPin("string").getConnectionPin().getValue();
        Main.console.appendText(str.asString());
        Main.console.appendText(" ");

        executeNext();
    }
}
