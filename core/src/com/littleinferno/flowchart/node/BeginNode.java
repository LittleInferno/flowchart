package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.codegen.CodeExecution;
import com.littleinferno.flowchart.codegen.JSBackend;
import com.littleinferno.flowchart.pin.Pin;

public class BeginNode extends Node {

    private Pin start;

    public BeginNode(Skin skin) {
        super("Begin", false, skin);

        start = addExecutionOutputPin("start");

        Button button = new Button(skin);
        left.add(button).expandX().fillX().height(30);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                CodeBuilder builder = new CodeBuilder(new JSBackend());
                String code = builder.genFun() + CodeBuilder.genVar() + gen(builder, getPin("start"));

                Gdx.app.log("", code);

                CodeExecution execute = new CodeExecution();
                execute.run(code);
            }
        });
        pack();
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {
        Pin.Connector next = start.getConnector();

        return next == null ? "" : next.parent.gen(builder, next.pin);
    }
}
