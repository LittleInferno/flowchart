package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;


public class CompareNode extends Node {
    public CompareNode(DataType type, Skin skin) {
        super("compare", true, skin);

        addExecutionInputPin();

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");

        addExecutionOutputPin("A > B");
        addExecutionOutputPin("A == B");
        addExecutionOutputPin("A < B");
    }

//    @Override
//    public void execute() {
//
//        Pin a = getPin("A").getConnectionPin();
//        Pin b = getPin("B").getConnectionPin();
//
//        if (a != null && b != null) {
//            Value aVal = a.getValue();
//            Value bVal = b.getValue();
//
//            Node next;
//
//            if (Value.equals(aVal, bVal).asBool())
//                next = getPin("A == B").getConnectionNode();
//            else if (Value.less(aVal, bVal).asBool())
//                next = getPin("A < B").getConnectionNode();
//            else
//                next = getPin("A > B").getConnectionNode();
//
//            if (next != null) {
//                try {
//                    next.execute();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        return null;
    }
}
