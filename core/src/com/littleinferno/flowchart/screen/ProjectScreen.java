package com.littleinferno.flowchart.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ProjectScreen extends AbstractScreen {

    float acc = 0;

    public ProjectScreen(ScreenManager screenManager) {
        super(screenManager, new Stage());

    }

    @Override
    public void update(float delta) {
        acc += delta;
        if (acc >= 3) {
            screenManager.setScreen("menu");
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);

        super.render(delta);


    }
}
