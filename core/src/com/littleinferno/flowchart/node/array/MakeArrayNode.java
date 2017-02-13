package com.littleinferno.flowchart.node.array;

import com.annimon.stream.Stream;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.BaseCodeGenerator;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;
import java.util.List;

public class MakeArrayNode extends Node {

    private final Pin.PinListener listener;
    private Pin array;
    private List<Pin> values;
    private int counter = 0;
    private DataType[] converts = {DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING};

    public MakeArrayNode() {
        super("make array", true);

        array = addDataOutputPin("array", converts);
        array.setArray(true);

        listener = t -> {
            array.setType(t);
            Stream.of(values).forEach(v -> v.setType(t));
        };

        array.addListener(listener);


        values = new ArrayList<>();

        Button add = new Button(Main.skin);
        right.addActor(add);


        add.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                if (!values.isEmpty() && values.get(0).getType() != DataType.UNIVERSAL)
                    values.add(addDataInputPin(values.get(0).getType(), "value" + counter++));
                else {
                    Pin pin = addDataInputPin("value" + counter++, converts);
                    pin.addListener(listener);

                    values.add(pin);
                }
            }
        });
        pack();
    }

    @Override
    public String gen(BaseCodeGenerator builder, Pin with) {

        ArrayList<String> val = new ArrayList<String>();

        for (Pin i : values) {
            Pin.Connector data = i.getConnector();
            if (i.getType() != DataType.EXECUTION)
                val.add(data.parent.gen(builder, data.pin));
        }

        return builder.makeArray(val);
    }
}
