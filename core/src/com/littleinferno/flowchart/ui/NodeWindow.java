package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class NodeWindow extends Window {

    boolean fill = false;

    float oldX;
    float oldY;
    float oldH;
    float oldW;


    public NodeWindow(Skin skin) {
        this("main", skin);
    }

    public NodeWindow(String title, Skin skin) {
        super(title, skin);
        setResizable(true);

        final TextButton button = new TextButton("\u25F1", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!fill) {
                    oldX = getX();
                    oldY = getY();
                    oldW = getWidth();
                    oldH = getHeight();
                    button.setText("\u25F1");
                } else {
                    setSize(oldW, oldH);
                    setPosition(oldX, oldY);
                    button.setText("\u25F3");
                }

                NodeWindow.this.setFillParent(fill = !fill);
            }
        });
        getTitleTable().add(button).size(getPadTop());

        setSize(400, 400);
    }
}
