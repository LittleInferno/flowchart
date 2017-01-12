package com.littleinferno.flowchart.node;

import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Variable;


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

        Node node = getItem("set").getPin().getConnectionNode();
        try {
            variable.setValue(node.evaluate());
        } catch (Exception e) {
            e.printStackTrace();
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

    private Variable variable;
}
