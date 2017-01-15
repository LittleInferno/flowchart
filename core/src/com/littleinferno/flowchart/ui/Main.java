package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.littleinferno.flowchart.node.BeginNode;

public class Main extends Stage {

    private static TabbedPane tabs;
    private ControlTable control;
    private Table container;
    private static Table activity = new Table();
    public static Skin skin;
    private static DragAndDrop dragAndDrop = new DragAndDrop();

    public static TextArea console;

    public Main() {
        super(new ScreenViewport());

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        container = new Table();
        container.setFillParent(true);
        addActor(container);

        control = new ControlTable(skin);

        container.add(control).expandY().fillY().width(300);
        container.add(activity).expand().fill();

        tabs = new TabbedPane(skin);
        activity.addActor(tabs);

        TabbedPane.Tab main = addWindow("main");

        BeginNode node = new BeginNode(skin);
        node.setPosition(200, 200);
        main.getContentTable().addActor(node);


        console = new TextArea("", skin);
        console.setPosition(100, 100);
        activity.addActor(console);

      //   setDebugAll(true);
    }

    static public DragAndDrop getDND() {
        return dragAndDrop;
    }

    static public void addSource(DragAndDrop.Source source) {
        dragAndDrop.addSource(source);
    }

    static public Table getActivity() {
        return activity;
    }

    static public TabbedPane.Tab addWindow(String name) {
        return tabs.addTab(name);
    }
}
