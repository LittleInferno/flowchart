package com.littleinferno.flowchart.node.array;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.littleinferno.flowchart.DataType;
import com.littleinferno.flowchart.codegen.CodeBuilder;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.pin.PinListener;
import com.littleinferno.flowchart.ui.Main;

import java.util.ArrayList;
import java.util.List;

public class MakeArrayNode extends Node {

    private Pin array;
    private List<Pin> values;
    private int counter = 0;
    private DataType[] converts = {DataType.BOOL, DataType.INT, DataType.FLOAT, DataType.STRING};

    PinListener defaultListener = new PinListener() {
        @Override
        public void typeChanged(DataType newType) {
            array.setType(newType);

            for (Pin p : values)
                p.setType(newType);
        }
    };

    public MakeArrayNode() {
        super("make array", true);

        array = addDataOutputPin("array", converts);
        array.setArray(true);
        array.addListener(defaultListener);
        values = new ArrayList<Pin>();

        Button add = new Button(Main.skin);
        right.addActor(add);


        add.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                if (!values.isEmpty() && values.get(0).getType() != DataType.UNIVERSAL)
                    values.add(addDataInputPin(values.get(0).getType(), "value" + counter++));
                else {
                    Pin pin = addDataInputPin("value" + counter++, converts);
                    pin.addListener(defaultListener);
                    values.add(pin);
                }
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
