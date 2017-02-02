package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.StringBuilder;

@Deprecated
public class VariableTable extends Table {

    private static int counter = 1;
    static private final VerticalGroup items = new VerticalGroup();
    private ScrollPane scroll;
    protected Skin skin;

    VariableTable(Skin skin) {
        this.skin = skin;
        scroll = new ScrollPane(items);
        Table container = new Table();
        container.add(scroll).expand().fillX().top();
        add(container).fill().expand();
    }

    void addComponent() {
        VariableItem var = new VariableItem(Integer.toString(counter++), skin);
        items.addActor(var);
    }

    //TODO remove it
    public static String gen() {

        SnapshotArray<Actor> children = items.getChildren();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < children.size; i++) {
            builder.append(((VariableItem) children.get(i)).variable.gen());
            builder.append('\n');
        }

        return String.valueOf(builder);
    }
}
