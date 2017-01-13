package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FunctionTable extends ComponentTable {

    private static int counter = 1;

    FunctionTable(Skin skin) {
        super(skin);
    }

    @Override
    void addComponent() {
        FunctionItem fun = new FunctionItem(Integer.toString(counter++), skin);
        items.addActor(fun);
    }
}
