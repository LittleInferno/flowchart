package com.littleinferno.flowchart.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.littleinferno.flowchart.Application;

public abstract class AbstractScreen extends ScreenAdapter {

    protected Application app;

    protected Stage stage;
    protected ScreenManager screenManager;

    public AbstractScreen(ScreenManager screenManager, Stage stage) {
        this.screenManager = screenManager;
        this.stage = stage;
    }

    public Application getApp() {
        return app;
    }

    abstract void update(float delta);

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
