package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.variable.Variable;

import java.util.HashMap;
import java.util.Map;

public class VariableSetNode extends Node {

    private Pin pin;
    private Pin next;
    private Variable variable;

    public VariableSetNode(Variable variable) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("variable name", variable.getName());

        init(new Node.NodeParams(0, 0, "Set" + variable.getName(), variable.getName(), true, attributes));
    }

    public VariableSetNode(Node.NodeParams nodeParams) {
        init(nodeParams);
    }

    private void init(Node.NodeParams nodeParams) {
        initFromHandle(nodeParams);
        variable = Project
                .instance()
                .getVariableManager()
                .getVariable((String) nodeParams.getAttr("variable name"));

        addExecutionInputPin();
        next = addExecutionOutputPin();

        this.pin = addDataInputPin("data", variable.getDataType());
        this.pin.setArray(variable.isArray());

        this.variable.addListener(this.pin::setArray);
        this.variable.addListener(this::setTitle);
        this.variable.addListener(this.pin::setType);
        this.variable.addListener(this::close);
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector data = pin.getConnector();
        String dataStr = data.parent.gen(builder, data.pin);

        Pin.Connector n = next.getConnector();
        String nextStr = n == null ? "" : n.parent.gen(builder, n.pin);

        String s = builder.makeStatement(builder.makeAssign(variable.getName(), dataStr));

        return String.format("%s%s", s, nextStr);
    }
}
