package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node {


    public LogNode() {
        super("Log", true);


        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        addDataInputPin(Value.Type.STRING, "string");
    }

    @Override
    void execute() {

        Node string = getItem("string").getPin().getConnectionNode();
        if (string != null) {
            try {
                Main.console.appendText(string.evaluate().asString());
                Main.console.newLineAtEnd();
            //    Gdx.app.log("", );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Node next = getItem("exec out").getPin().getConnectionNode();

        if (next != null) {
            try {
                next.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
