package com.littleinferno.flowchart;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.gui.SceneScreen;

public class Flowchart extends Game {

    private Screen scene;
    private static boolean change;

    @Override
    public void create() {

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            VisUI.load("X2/uiskin.json");
        else
            VisUI.load("X1/uiskin.json");

        scene = new SceneScreen();
        setScreen(scene);

    }

    @Override
    public void dispose() {

        scene.dispose();

        VisUI.dispose();
    }
}
