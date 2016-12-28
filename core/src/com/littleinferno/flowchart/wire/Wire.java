package com.littleinferno.flowchart.wire;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.littleinferno.flowchart.pin.Pin;

import java.util.ArrayList;

/**
 * Created by danil on 11.12.2016.
 */

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

        Vector2 b = new Vector2(begin.getX(), begin.getY());
        b = begin.localToStageCoordinates(b);

        Vector2 e = new Vector2(end.getX(), end.getY());
        e = end.localToStageCoordinates(e);

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.rectLine(b.x, b.y, e.x, e.y, 5);
        renderer.end();

        batch.begin();
    }

    private void calculate() {

        // begPoint = begin.getPosition();
        //   endPoint = end.getPosition();

    }


    private Pin begin;
    private Pin end;
    private ShapeRenderer renderer;

    private Vector2 begPoint;
    private Vector2 endPoint;
}
