package com.littleinferno.flowchart.node.array;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class MakeArrayNode extends Node {

    private List<Pin> values;
    private int counter = 0;

    public MakeArrayNode(final DataType type, Skin skin) {
        super("make array", true, skin);

        addDataOutputPin(type, "array").setArray(true);
        values = new ArrayList<Pin>();

        Button add = new Button(skin);
        right.addActor(add);//.expandX().fillX().height(30);


        add.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                values.add(addDataInputPin(type, "valeue" + counter++));
            }
        });
        pack();
    }

    @Override
    public String gen(CodeBuilder builder, Pin with) {

        ArrayList<String> val = new ArrayList<String>();

        for (Pin i : values) {
            Pin.Connector data = i.getConnector();
            if (i.getType() != DataType.EXECUTION)
                val.add(data.parent.gen(builder, data.pin));
        }

        return builder.createArray(val);
    }
}
