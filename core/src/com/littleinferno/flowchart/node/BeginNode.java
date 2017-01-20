package com.littleinferno.flowchart.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.codegen.Builder;
import com.littleinferno.flowchart.codegen.CodeExecution;
import com.littleinferno.flowchart.pin.Pin;

public class BeginNode extends Node {
    public BeginNode(Skin skin) {
        super("Begin", false, skin);

        addExecutionOutputPin("start");

        Button button = new Button(skin);
        left.add(button).expandX().fillX().height(30);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String code = Builder.genFun() + Builder.genVar() + gen(getPin("start"));

                Gdx.app.log("", code);

                CodeExecution execute = new CodeExecution();
                execute.run(code);
            }
        });
    }

    @Override
    public String gen(Pin with) {
        Pin.Connector next = getPin("start").getConnector();

        return next == null ? "" : next.parent.gen(next.pin);
    }
}
