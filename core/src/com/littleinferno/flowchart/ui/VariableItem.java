package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class VariableItem extends Table {
    public VariableItem(Skin skin, CharSequence name) {
        setSize(200, 30);
        add(new TextField(name.toString(), skin)).expand().fill();
        add(new Button(skin)).expand();
    }
}
