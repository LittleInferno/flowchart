package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class SceneScreen extends ScreenAdapter {

    Scenetmp sceneUi;

    public SceneScreen() {
        sceneUi = new Scenetmp();

    }

    @Override
    public void resize(int width, int height) {
        sceneUi.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sceneUi.act();
        sceneUi.draw();
    }

    @Override
    public void dispose() {
        sceneUi.dispose();
        super.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(sceneUi.getInputMultiplexer());
    }




}
