package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class MenuScreen extends ScreenAdapter {

    private com.littleinferno.flowchart.gui.menu.Menu mainMenu;

    public MenuScreen() {
        mainMenu = new com.littleinferno.flowchart.gui.menu.Menu();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainMenu.act();
        mainMenu.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenu);
    }
}
