package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

@Deprecated
public class Main extends Stage {

    public static Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    public static VisUI.SkinScale scale = VisUI.SkinScale.X1;


}
