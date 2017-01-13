package com.littleinferno.flowchart.node;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class BeginNode extends Node {
    public BeginNode() {
        super("Begin");

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
    void execute() {
        Node next = getItem("start").getPin().getConnectionNode();

        if (next != null)
            try {
                next.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
