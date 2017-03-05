package com.littleinferno.flowchart.wire;

import com.annimon.stream.Stream;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.scene.Scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WireManager extends Actor {

    public static final int NULL_ID = 0;

    private List<Wire> wires;

    private int counter;
    private final Scene scene;
    private Pin first;
    private ShapeRenderer renderer;

    public WireManager(Scene scene) {
        this.scene = scene;
        renderer = new ShapeRenderer();
        wires = new ArrayList<>();
        counter = 1;
    }

    public void init(WireManagerHandle wireManagerHandle) {

        counter = wireManagerHandle.counter;
        Stream.of(wireManagerHandle.wireHandles)
                .forEach(this::add);
    }

    private void add(Wire.WireHandle wireHandle) {
        Pin begin = scene.getNodeManager()
                .getNode(wireHandle.beginNodeID).getPin(wireHandle.beginPinName);

        Pin end = scene.getNodeManager()
                .getNode(wireHandle.endNodeID).getPin(wireHandle.endPinName);

        begin.connect(end);
    }

    public int add(Pin begin, Pin end) {
        wires.add(new Wire(begin, end, counter));
        return counter++;
    }

    public int remove(int id) {

        for (Iterator<Wire> i = wires.iterator(); i.hasNext(); ) {
            if (i.next().getId() == id)
                i.remove();
        }

        return NULL_ID;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Line);
        Stream.of(wires).forEach(wire -> wire.draw(renderer));
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

    public WireManagerHandle getHandle() {
        return new WireManagerHandle(counter,
                Stream.of(wires).map(Wire::getHandle).toList());
    }

    public static class WireManagerHandle {
        int counter;
        List<Wire.WireHandle> wireHandles;

        public WireManagerHandle() {
        }

        public WireManagerHandle(int counter, List<Wire.WireHandle> wireHandles) {
            this.counter = counter;
            this.wireHandles = wireHandles;
        }
    }

}
