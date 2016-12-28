package com.littleinferno.flowchart.wire;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.littleinferno.flowchart.pin.Pin;

import java.util.Map;

public class WireConnector {

    public static int add(Pin begin, Pin end) {

        wires.put(counter, new Wire(begin, end));
        return counter++;
    }

    public static int remove(int i) {

        wires.remove(i);

        return -1;
    }


    public static void add(Pin pin) {

        if (first == null)
            first = pin;
        else {
            first.connect(pin);
            first = null;
        }
    }

    private static Map<Integer, Wire> wires;

    private static Integer counter;

    private static Pin first;

    public static Stage base;
}
