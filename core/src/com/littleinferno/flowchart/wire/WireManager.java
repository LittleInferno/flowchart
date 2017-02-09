package com.littleinferno.flowchart.wire;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.littleinferno.flowchart.pin.Pin;

import java.util.HashMap;
import java.util.Map;

public class WireManager extends Actor {

    public WireManager() {
        renderer = new ShapeRenderer();
        wires = new HashMap<>();
        counter = 0;
    }

    public int add(Pin begin, Pin end) {

        wires.put(counter, new Wire(begin, end));
        return counter++;
    }

    public int remove(int i) {

        wires.remove(i);

        return -1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Line);
        for (Map.Entry<Integer, Wire> entry : wires.entrySet()) {
            entry.getValue().draw(renderer);

        }

        renderer.end();
        batch.begin();
    }

    public static void add(Pin pin) {

        if (first == null)
            first = pin;
        else {
            first.connect(pin);
            first = null;
        }
    }

    private Map<Integer, Wire> wires;

    private int counter;

    private static Pin first;

    private ShapeRenderer renderer;
}
