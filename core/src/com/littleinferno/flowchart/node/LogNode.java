package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.codegen.PrintStatement;
import com.littleinferno.flowchart.codegen.Statement;
import com.littleinferno.flowchart.codegen.StatementGeneratable;
import com.littleinferno.flowchart.codegen.StatementNext;
import com.littleinferno.flowchart.value.Value;

public class LogNode extends Node implements StatementGeneratable {


    public LogNode(Skin skin) {
        super("Log", true, skin);

        addExecutionInputPin();
        addExecutionOutputPin();

        addDataInputPin(Value.Type.STRING, "string");
    }

    @Override
    public void execute() {
        genStatement().execute();
    }

    @Override
    public Statement genStatement() {

        ExpressionGeneratable expression = (ExpressionGeneratable) getPin("string").getConnectionNode();
        PrintStatement printStatement = new PrintStatement(expression.genExpression());

        Node next = getPin("exec out").getConnectionNode();
        if (next != null) {
            return new StatementNext(printStatement, ((StatementGeneratable) next).genStatement());
        }

        return printStatement;
    }
}
