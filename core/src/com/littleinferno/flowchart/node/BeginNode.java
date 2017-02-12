package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.gui.Scenetmp;
import com.littleinferno.flowchart.pin.Pin;

public class BeginNode extends Node {

    private Pin start;

    public BeginNode(Scenetmp sceneUi) {
        super("Begin", false);

        start = addExecutionOutputPin("start");

        sceneUi.setBegin(builder -> gen(builder, start));
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector next = start.getConnector();

        return next == null ? "" : next.parent.gen(builder, next.pin);
    }
}
