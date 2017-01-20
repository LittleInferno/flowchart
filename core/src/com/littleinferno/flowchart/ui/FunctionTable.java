package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.StringBuilder;

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

    //TODO remove it
    public static String gen() {

        SnapshotArray<Actor> children = items.getChildren();

        StringBuilder builder = new StringBuilder();

        for (Actor i : children) {

            builder.append(((FunctionItem) i).function.getBeginNode().gen(((FunctionItem) i).
                    function.getBeginNode().getPin("exec out")));
            builder.append('\n');
        }

        return String.valueOf(builder);
    }

}
