package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.codegen.IfStatement;
import com.littleinferno.flowchart.codegen.Statement;
import com.littleinferno.flowchart.codegen.StatementGeneratable;
import com.littleinferno.flowchart.value.Value;

public class IfNode extends Node implements StatementGeneratable {
    public IfNode(Skin skin) {
        super("if", true, skin);

        addExecutionInputPin();
        addDataInputPin(Value.Type.BOOL, "Condition");

        addExecutionOutputPin("True");
        addExecutionOutputPin("False");
    }

    @Override
    public void execute() throws Exception {
        genStatement().execute();
    }

    @Override
    public Statement genStatement() {

        ExpressionGeneratable condition = (ExpressionGeneratable) getPin("Condition").getConnectionNode();

        StatementGeneratable ifStatement = (StatementGeneratable) getPin("True").getConnectionNode();
        StatementGeneratable elseStatement = (StatementGeneratable) getPin("False").getConnectionNode();

        return new IfStatement(
                condition.genExpression(),
                ifStatement.genStatement(),
                elseStatement != null ? elseStatement.genStatement() : null);
    }
}
