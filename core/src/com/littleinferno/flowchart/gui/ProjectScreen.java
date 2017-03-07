package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class ProjectScreen extends ScreenAdapter {

    private UIScene uiScene;

    public ProjectScreen(UIScene uiScene) {
        this.uiScene = uiScene;
        Gdx.gl.glClearColor(20 / 255.f, 20 / 255.f, 20 / 255.f, 0);
    }

    @Override
    public void resize(int width, int height) {
        uiScene.getViewport().update(width, height, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiScene.act();
        uiScene.draw();
    }

    @Override
    public void dispose() {
        uiScene.dispose();
        super.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(uiScene.getInputMultiplexer());
    }
}
