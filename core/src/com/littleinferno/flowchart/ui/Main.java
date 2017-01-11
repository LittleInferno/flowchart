package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Main extends Stage {

    public Main() {
        super(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        container = new Table();
        container.setFillParent(true);
        addActor(container);

        control = new ControlTable(skin);
        activity = new Table();

        container.add(control).expandY().fillY().width(300);
        container.add(activity).expand().fill();

        final NodeWindow mainWindow = new NodeWindow("main",skin);
        activity.addActor(mainWindow);

        dragAndDrop = new DragAndDrop();

        dragAndDrop.addTarget(new DragAndDrop.Target(mainWindow) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Actor node = (Actor) payload.getObject();
                node.setPosition(x, y);
                mainWindow.addActor(node);
            }
        });
    }

    static public DragAndDrop getDND() {
        return dragAndDrop;
    }

    private ControlTable control;
    private Table container;
    private Table activity;
    private Skin skin;
    private static DragAndDrop dragAndDrop = new DragAndDrop();
}
