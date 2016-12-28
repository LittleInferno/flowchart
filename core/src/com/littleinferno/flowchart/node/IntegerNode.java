package com.littleinferno.flowchart.node;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.littleinferno.flowchart.pin.Pin;

/**
 * Created by danil on 26.12.2016.
 */

public class IntegerNode extends Node {
    public IntegerNode(Vector2 position) {
        super(position, "Integer constant");

        addDataOutputPin(Pin.Data.INT, "data");

        data = new char[1000];
        TextField field = new TextField("", skin);
        left.add(field);

        setDebug(true,true);
    }

    private char[] data;
    private int pos;
}
