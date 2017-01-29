package com.littleinferno.flowchart.wire;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.littleinferno.flowchart.Connection;
import com.littleinferno.flowchart.pin.Pin;

public class Wire extends Actor {

    public Wire(Pin begin, Pin end) {
        this.begin = begin;
        this.end = end;
        renderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);

        Vector2 b = begin.getLocation();

        if (begin.getConnection() == Connection.INPUT) b.add(new Vector2(8, 8));
        else b.add(new Vector2(86, 8));

        Vector2 e = end.getLocation();

        if (end.getConnection() == Connection.INPUT) e.add(new Vector2(8, 8));
        else e.add(new Vector2(86, 8));

        float xLength = (e.x - b.x);
        float yLength = Math.abs(e.y - b.y);

        if (xLength > -100 || xLength < 100) xLength = 100;

        if (begin.getConnection() == Connection.INPUT) {
            float tmp = b.x;
            b.x = e.x;
            e.x = tmp;

            tmp = b.y;
            b.y = e.y;
            e.y = tmp;
        }

        float xHalfLength = xLength / 2;

        float cx1 = b.x + xHalfLength;
        float cx2 = e.x - xHalfLength;

        renderer.curve(b.x, b.y, cx1, b.y, cx2, e.y, e.x, e.y, 50);

        Gdx.gl20.glLineWidth(2);
        renderer.end();

        batch.begin();
    }

//    private void calculate() {
//
//        // begPoint = begin.getPosition();
//        //   endPoint = end.getPosition();
//
//    }


    private Pin begin;
    private Pin end;
    private ShapeRenderer renderer;
}
