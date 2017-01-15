package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.codegen.FunctionCall;
import com.littleinferno.flowchart.codegen.Statement;
import com.littleinferno.flowchart.codegen.StatementGeneratable;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.value.Value;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallNode extends Node implements StatementGeneratable, ExpressionGeneratable {
    private Function function;

    public FunctionCallNode(Function function, Skin skin) {
        super(function.getName(), true, skin);

        addExecutionInputPin("exec in");
        addExecutionOutputPin("exec out");

        this.function = function;
        function.addNode(this);
    }

    @Override
    public void execute() {
        function.setCurrentCall(this);

        Array<Pin> inputs = getInput();
        FunctionBeginNode begNode = function.getBeginNode();
        for (Pin i : inputs) {
            if (i.getType() != Value.Type.EXECUTION)
                begNode.getPin(i.getName()).setValue(i.getConnectionPin().getValue());
        }
        begNode.execute();

        executeNext();
    }

    @Override
    public void eval() {
    }

    @Override
    public Expression genExpression() {
        com.littleinferno.flowchart.codegen.Functions.set(function.getName(),
                new com.littleinferno.flowchart.codegen.Function(function.getBeginNode().genStatement()));

        Array<Pin> inputs = getInput();

        List expressions = new ArrayList<Expression>(inputs.size);

        for (int i = 0; i < inputs.size; ++i) {
            expressions.set(i, ((ExpressionGeneratable) inputs.get(i).getConnectionNode()).genExpression());
        }

        return new FunctionCall(function.getName(), expressions);
    }

    @Override
    public Statement genStatement() {
        com.littleinferno.flowchart.codegen.Functions.set(function.getName(),
                new com.littleinferno.flowchart.codegen.Function(function.getBeginNode().genStatement()));

        Array<Pin> inputs = getInput();

        List expressions = new ArrayList<Expression>(inputs.size);

        for (int i = 0; i < inputs.size; ++i) {
            if (inputs.get(i).getType() != Value.Type.EXECUTION)
                expressions.set(i, ((ExpressionGeneratable) inputs.get(i).getConnectionNode()).genExpression());
        }

        return new FunctionCall(function.getName(), expressions);
    }
}
