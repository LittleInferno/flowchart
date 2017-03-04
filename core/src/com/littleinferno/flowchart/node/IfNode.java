package com.littleinferno.flowchart.node;

import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.pin.Pin;

public class IfNode extends Node {
    private Pin condition;
    private Pin truePath;
    private Pin falsePath;

    public IfNode() {
        this(new NodeHandle("if", true));
    }

    public IfNode(NodeHandle nodeHandle) {
        super(nodeHandle);

        addExecutionInputPin();
        condition = addDataInputPin("Condition", DataType.BOOL);

        truePath = addExecutionOutputPin("True");
        falsePath = addExecutionOutputPin("False");
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        Pin.Connector cond = condition.getConnector();

        Pin.Connector tr = truePath.getConnector();
        Pin.Connector fl = falsePath.getConnector();

        String conditionStr = cond.parent.gen(builder, cond.pin);
        String trueString = tr.parent.gen(builder, tr.pin);
        String falseString = fl == null ? "" : fl.parent.gen(builder, fl.pin);

        String string;
        if (fl != null)
            string = builder.makeIfElse(conditionStr,
                    builder.makeBlock(trueString), builder.makeBlock(falseString));
        else
            string = builder.makeIf(conditionStr, builder.makeBlock(trueString));

        return string;
    }
}
