package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.value.Value;

public class TestASTGenerate extends Node {
    public TestASTGenerate(Skin skin) {
        super("TestASTGenerate", true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(Value.Type.INT, "code");
    }


    @Override
    public void execute() {

        ExpressionGeneratable gen = (ExpressionGeneratable) getPin("code").getConnectionNode();

        Expression expression = gen.genExpression();
        Value value = expression.eval();
        Gdx.app.log("", value.asString());

        executeNext();
    }
}
