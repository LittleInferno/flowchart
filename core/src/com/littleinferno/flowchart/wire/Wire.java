package com.littleinferno.flowchart.wire;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.pin.Pin;

class Wire {
    private Pin begin;
    private Pin end;
    private int id;

    Wire(Pin begin, Pin end, int id) {
        this.begin = begin;
        this.end = end;
        this.id = id;
    }

    void draw(ShapeRenderer renderer) {
        renderer.setColor(begin.getTypeColor());

        Vector2 b = begin.getLocation();
        Vector2 e = end.getLocation();

        float xLength = (e.x - b.x);

        if (xLength > -100 || xLength < 100) xLength = 100;

        if (begin.getConnection() == Connection.INPUT) {
            b.add(new Vector2(8, 8));
            e.add(new Vector2(begin.getWidth() - 14, 8));

            float tmp = b.x;
            b.x = e.x;
            e.x = tmp;

            tmp = b.y;
            b.y = e.y;
            e.y = tmp;
        } else {
            b.add(new Vector2(begin.getWidth() - 14, 8));
            e.add(new Vector2(8, 8));
        }

        float xHalfLength = xLength / 2;

        float cx1 = b.x + xHalfLength;
        float cx2 = e.x - xHalfLength;

        renderer.curve(b.x, b.y, cx1, b.y, cx2, e.y, e.x, e.y, 20);
    }

    public int getId() {
        return id;
    }

    public WireHandle getHandle() {
        return new WireHandle(begin.getNode().getId(), begin.getName(),
                end.getNode().getId(), end.getName());
    }

    public static class WireHandle {
        String beginNodeID;
        String beginPinName;
        String endNodeID;
        String endPinName;

        public WireHandle() {
        }

        public WireHandle(String beginNodeID, String beginPinName, String endNodeID, String endPinName) {
            this.beginNodeID = beginNodeID;
            this.beginPinName = beginPinName;
            this.endNodeID = endNodeID;
            this.endPinName = endPinName;
        }
    }
}
