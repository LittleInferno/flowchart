package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node {


    public LogNode(Vector2 position) {
        super(position, "Log");


        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        addDataInputPin(Value.Type.STRING, "string");
    }

    @Override
    void execute() {

        Node string = get("string").getConnectionNode();
        if (string != null) {
            try {
                Gdx.app.log("", string.evaluate().asString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Node next = get("exec out").getConnectionNode();

        if (next != null) {
            try {
                next.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
