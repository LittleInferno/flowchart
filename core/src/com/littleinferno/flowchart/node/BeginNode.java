package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.project.Project;

public class BeginNode extends Node {

    private Pin start;

    public BeginNode() {
        this(new NodeHandle("Begin", false));
    }

    public BeginNode(NodeHandle nodeHandle) {
        super(nodeHandle);

        start = addExecutionOutputPin("start");
        Project.instance().setProgramStart(builder -> gen(builder, start));
    }


    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        Pin.Connector next = start.getConnector();

        return next == null ? "" : next.parent.gen(builder, next.pin);
    }
}
