package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Variable;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;


public class VariableSetNode extends Node {

    public VariableSetNode(Vector2 pos, Variable variable) {
        super(pos, String.format("Set %s", variable.getName()));

        this.variable = variable;
        variable.addNode(this);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        addDataInputPin(variable.getValueType(), "set");
    }

    @Override
    void execute() {

        Node node = get("set").getConnectionNode();
        try {
            variable.setValue(node.evaluate());
        } catch (Exception e) {
            e.printStackTrace();
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

    private Variable variable;
}
