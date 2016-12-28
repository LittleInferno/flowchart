package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class VariableTable extends Table {


    public VariableTable(Skin skin) {

        setDebug(true, true);
        setWidth(200.f);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        Label label = new Label("Variables", skin);
        horizontalGroup.left();
        horizontalGroup.addActor(label);
        horizontalGroup.addActor(label);
        //horizontalGroup.();

        add(horizontalGroup).fillX();

        //add(new Button(skin)).width(16);
        row();

        VerticalGroup list = new VerticalGroup();
        list.setDebug(true, true);
        list.addActor(new VariableItem(skin, "sfd"));
        list.addActor(new VariableItem(skin, "fds"));
        add(list).expandX().fillX();
    }


}
