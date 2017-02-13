package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.gui.SceneUi;
import com.littleinferno.flowchart.pin.Pin;

public class BeginNode extends Node {

    private Pin start;

    public BeginNode(SceneUi sceneUi) {
        super("Begin", false);

        start = addExecutionOutputPin("start");

        sceneUi.setBegin(builder -> gen(builder, start));
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        Pin.Connector next = start.getConnector();

        return next == null ? "" : next.parent.gen(builder, next.pin);
    }
}
