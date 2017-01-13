package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public abstract class ComponentTable extends Table {

    protected VerticalGroup items;
    protected ScrollPane scroll;
    protected Skin skin;

    ComponentTable(Skin skin) {
        this.skin = skin;

        items = new VerticalGroup();
        scroll = new ScrollPane(items);
        Table container = new Table();
        container.add(scroll).expand().fillX().top();
        add(container).fill().expand();
    }

    abstract void addComponent();
}
