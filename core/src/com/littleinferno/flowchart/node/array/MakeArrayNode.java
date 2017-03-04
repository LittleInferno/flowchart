package com.littleinferno.flowchart.node.array;

import com.annimon.stream.Stream;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;
import java.util.List;

public class MakeArrayNode extends Node {

    private Pin.PinListener listener;
    private Pin array;
    private List<Pin> values;
    private int counter = 0;


    public MakeArrayNode(NodeHandle nodeHandle) {
        super(nodeHandle);
    }

    public MakeArrayNode() {
        this(new NodeHandle("make array", true));

        array = addDataOutputPin("array", Pin.DEFAULT_CONVERT);
        array.setArray(true);

        listener = t -> {
            array.setType(t);
            Stream.of(values).forEach(v -> v.setType(t));
        };

        array.addListener(listener);


        values = new ArrayList<>();

        VisTextButton add = new VisTextButton("add");
        right.addActor(add);


        add.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (array.getType() != DataType.UNIVERSAL)
                    values.add(addDataInputPin("value" + counter++, array.getType()));
                else if (!values.isEmpty() && values.get(0).getType() != DataType.UNIVERSAL)
                    values.add(addDataInputPin("value" + counter++, values.get(0).getType()));
                else {
                    Pin pin = addDataInputPin("value" + counter++, Pin.DEFAULT_CONVERT);
                    pin.addListener(listener);

                    values.add(pin);
                }
            }
        });
        pack();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {
        return builder.makeArray((ArrayList<Pin>) values);//TODO remove it
    }
}
