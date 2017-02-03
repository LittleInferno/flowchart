package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.StringBuilder;
import com.littleinferno.flowchart.codegen.CodeBuilder;

@Deprecated
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
    public static String gen(CodeBuilder builder) {

        SnapshotArray<Actor> children = items.getChildren();

        StringBuilder stringBuilder = new StringBuilder();

        for (Actor i : children) {

            stringBuilder.append(((FunctionItem) i).function.getBeginNode().gen(builder, ((FunctionItem) i).
                    function.getBeginNode().getPin("exec out")));
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

}
