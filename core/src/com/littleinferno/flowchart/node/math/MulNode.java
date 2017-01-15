package com.littleinferno.flowchart.node.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.BinaryExpression;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.value.Value;

public class MulNode extends Node implements ExpressionGeneratable {

    public MulNode(Value.Type type, Skin skin) {
        super("mul", true, skin);

        addDataInputPin(type, "A");
        addDataInputPin(type, "B");
        addDataOutputPin(type, "A * B");
    }

    @Override
    public void eval() throws Exception {
        getPin("A * B").setValue(genExpression().eval());
    }

    @Override
    public Expression genExpression() {
        ExpressionGeneratable a = (ExpressionGeneratable) getPin("A").getConnectionNode();
        ExpressionGeneratable b = (ExpressionGeneratable) getPin("B").getConnectionNode();

        return new BinaryExpression(a.genExpression(), b.genExpression(), BinaryExpression.Operator.mul);
    }
}
