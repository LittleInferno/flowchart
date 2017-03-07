package com.littleinferno.flowchart.screen;

import com.badlogic.gdx.Gdx;
import com.littleinferno.flowchart.gui.menu.Menu;

public class MenuScreen extends AbstractScreen {

    public MenuScreen(ScreenManager screenManager) {
        super(screenManager, new Menu());
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        super.render(delta);

        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
