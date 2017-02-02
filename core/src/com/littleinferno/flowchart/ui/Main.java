package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.gui.ControlTable;
import com.littleinferno.flowchart.node.BeginNode;
import com.littleinferno.flowchart.node.BoolNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.StringNode;
import com.littleinferno.flowchart.node.math.AddNode;

public class Main extends Stage {

    private static TabbedPane tabs;
    private ControlTable control;
    private Table container;
    public static Table activity = new Table();
    public static Skin skin;
    private static DragAndDrop dragAndDrop = new DragAndDrop();
    public static VisUI.SkinScale scale = VisUI.SkinScale.X1;

    //TODO remove it
    public static DragAndDrop dndFunctions = new DragAndDrop();

    public static TextArea console;

    public Main() {
        super(new ScreenViewport());
        VisUI.load(scale);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        container = new Table();
        container.setFillParent(true);
        addActor(container);

        control = new ControlTable();

        if (scale == VisUI.SkinScale.X1)
            container.add(control).growY().width(310);
        else
            container.add(control).growY().width(460);
        container.add(activity).expand().fill();

        tabs = new TabbedPane(skin);
        activity.addActor(tabs);

        TabbedPane.Tab main = addWindow("main");

        BeginNode node = new BeginNode(skin);
        node.setPosition(200, 200);
        main.getContentTable().addActor(node);

        AddNode node1 = new AddNode();
        node.setPosition(300, 200);
        main.getContentTable().addActor(node1);
        AddNode node2 = new AddNode();
        node2.setPosition(300, 200);
        main.getContentTable().addActor(node2);

        IntegerNode node3 = new IntegerNode();
        node3.setPosition(300, 200);
        main.getContentTable().addActor(node3);

        StringNode node4 = new StringNode();
        node4.setPosition(300, 200);
        main.getContentTable().addActor(node4);

        BoolNode node5 = new BoolNode();
        node5.setPosition(300, 200);
        main.getContentTable().addActor(node5);

        console = new TextArea("", skin);
        console.setPosition(100, 100);
        activity.addActor(console);

    //    setDebugAll(true);
    }

    static public DragAndDrop getDND() {
        return dragAndDrop;
    }

    static public void addSource(DragAndDrop.Source source) {
        dragAndDrop.addSource(source);
    }

    static public void addSourceF(DragAndDrop.Source source) {
        dndFunctions.addSource(source);
    }

    static public Table getActivity() {
        return activity;
    }

    static public TabbedPane.Tab addWindow(String name) {
        return tabs.addTab(name);
    }
}
