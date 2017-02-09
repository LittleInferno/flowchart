package com.littleinferno.flowchart;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.gui.SceneScreen;

public class Flowchart extends Game {

    private Screen scene;
    private static boolean change;

//    @Override
//    public void resize(int width, int height) {
//        super.resize(width, height);
//        main.getViewport().update(width, height, true);
//    }

    @Override
    public void create() {
        VisUI.load();

        scene = new SceneScreen();
        setScreen(scene);

    }

    @Override
    public void render() {
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // main.act();
        //  main.draw();

        super.render();
    }

//    @Override
//    public void dispose() {
//
//        main.dispose();
//    }
}
