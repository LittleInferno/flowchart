package com.littleinferno.flowchart.wire;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.pin.Pin;

class Wire {
    private Pin begin;
    private Pin end;

    Wire(Pin begin, Pin end) {
        this.begin = begin;
        this.end = end;
    }

    void draw(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);

        Vector2 b = begin.getLocation();
        Vector2 e = end.getLocation();

        float xLength = (e.x - b.x);

        if (xLength > -100 || xLength < 100) xLength = 100;

        if (begin.getConnection() == Connection.INPUT) {
            b.add(new Vector2(8, 8));
            e.add(new Vector2(86, 8));

            float tmp = b.x;
            b.x = e.x;
            e.x = tmp;

            tmp = b.y;
            b.y = e.y;
            e.y = tmp;
        } else {
            b.add(new Vector2(86, 8));
            e.add(new Vector2(8, 8));
        }

        float xHalfLength = xLength / 2;

        float cx1 = b.x + xHalfLength;
        float cx2 = e.x - xHalfLength;

        renderer.curve(b.x, b.y, cx1, b.y, cx2, e.y, e.x, e.y, 20);
    }

}
