package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class VariableTable extends ComponentTable {

    private static int counter = 1;

    VariableTable(Skin skin) {
        super(skin);
    }

    @Override
    void addComponent() {
        VariableItem var = new VariableItem(Integer.toString(counter++), skin);
        items.addActor(var);
    }
}
