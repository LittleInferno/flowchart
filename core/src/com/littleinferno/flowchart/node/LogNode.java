package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node {


    public LogNode() {
        super("Log", true);

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
