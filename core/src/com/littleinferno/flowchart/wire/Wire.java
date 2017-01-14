package com.littleinferno.flowchart.wire;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);

        Vector2 b = begin.getLocation();

        if (begin.getConnection() == Pin.input) b.add(new Vector2(8, 8));
        else b.add(new Vector2(86, 8));


        Vector2 e = end.getLocation();

        if (end.getConnection() == Pin.input) e.add(new Vector2(8, 8));
        else e.add(new Vector2(86, 8));

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.line(b.x, b.y, 0.f, e.x, e.y, 0.f);
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
