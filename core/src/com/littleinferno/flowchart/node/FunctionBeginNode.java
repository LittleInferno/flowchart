package com.littleinferno.flowchart.node;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.Function;
import com.littleinferno.flowchart.codegen.Expression;
import com.littleinferno.flowchart.codegen.ExpressionGeneratable;
import com.littleinferno.flowchart.codegen.Statement;
import com.littleinferno.flowchart.codegen.StatementGeneratable;

public class FunctionBeginNode extends Node implements StatementGeneratable, ExpressionGeneratable {
    public FunctionBeginNode(Function function, Skin skin) {
        super(function.getName(), false, skin);

        addExecutionOutputPin();


        Button button = new Button(skin);
        left.add(button).expandX().fillX().height(30);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                execute();
            }
        });
    }

    @Override
    public void eval() {
    }

    @Override
    public void execute() {
        genStatement().execute();
    }

    @Override
    public Statement genStatement() {
        StatementGeneratable statement = (StatementGeneratable) getPin("exec out").getConnectionNode();
        return statement.genStatement();
    }

    @Override
    public Expression genExpression() {

        return null;
    }
}
