package com.littleinferno.flowchart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.littleinferno.flowchart.tempgui.MainMenu;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.wire.WireManager;

public class Flowchart extends ApplicationAdapter {

    private Main main;
    private MainMenu mainMenu;

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        main.getViewport().update(width, height, true);
//        mainMenu.getViewport().update(width, height, true);
    }

    @Override
    public void create() {

        main = new Main();
        WireManager.base = main;

        Gdx.input.setInputProcessor(main);

//        mainMenu = new MainMenu();
//        Gdx.input.setInputProcessor(mainMenu);

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.act();
        main.draw();

//        mainMenu.act();
//        mainMenu.draw();
    }

    @Override
    public void dispose() {
//        mainMenu.dispose();

        main.dispose();
    }
}
