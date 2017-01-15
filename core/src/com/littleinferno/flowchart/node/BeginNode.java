package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.codegen.Statement;
import com.littleinferno.flowchart.codegen.StatementGeneratable;

public class BeginNode extends Node implements StatementGeneratable{
    public BeginNode(Skin skin) {
        super("Begin", false, skin);

        addExecutionOutputPin("start");

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
    public void execute() {
        genStatement().execute();
    }

    @Override
    public Statement genStatement() {
        StatementGeneratable statement = (StatementGeneratable) getPin("start").getConnectionNode();
        return statement.genStatement();
    }
}
