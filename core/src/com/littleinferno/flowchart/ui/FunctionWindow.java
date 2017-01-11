package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by danil on 11.01.2017.
 */

public class FunctionWindow extends NodeWindow {
    public FunctionWindow(String name, Skin skin) {
        super(name, skin);


        final TextButton button = new TextButton("\u00D7", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FunctionWindow.this.setVisible(false);
            }
        });
        getTitleTable().add(button).size(getPadTop());
    }


}
