package com.littleinferno.flowchart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.wire.WireConnector;

public class Flowchart extends ApplicationAdapter {

    private Main main;

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        main.getViewport().update(width, height, true);
    }

    @Override
    public void create() {

       // Gdx.app.log("", Eval.me("33*3").toString());
        main = new Main();
        WireConnector.base = main;

        Gdx.input.setInputProcessor(main);

    }

    @Override
    public void render() {
        //   Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.act();
        main.draw();
    }

    @Override
    public void dispose() {
        main.dispose();
    }
}
