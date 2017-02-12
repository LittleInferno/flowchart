package com.littleinferno.flowchart.wire;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.littleinferno.flowchart.pin.Pin;

import java.util.HashMap;
import java.util.Map;

public class WireManager extends Actor {

    public static final int NULL_ID = 0;

    public WireManager() {
        renderer = new ShapeRenderer();
        wires = new HashMap<>();
        counter = 1;
    }

    public int add(Pin begin, Pin end) {
        wires.put(counter, new Wire(begin, end));
        return counter++;
    }

    public int remove(int i) {

        wires.remove(i);

        return NULL_ID;
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

    public void add(Pin pin) {

        if (first == null)
            first = pin;
        else {
            first.connect(pin);
            first = null;
        }
    }

    private Map<Integer, Wire> wires;

    private int counter;

    private Pin first;

    private ShapeRenderer renderer;
}
