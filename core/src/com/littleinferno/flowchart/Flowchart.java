package com.littleinferno.flowchart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.littleinferno.flowchart.node.BeginNode;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.LogNode;
import com.littleinferno.flowchart.node.StringNode;
import com.littleinferno.flowchart.ui.ControlTable;
import com.littleinferno.flowchart.ui.FunctionItem;
import com.littleinferno.flowchart.wire.WireConnector;

public class Flowchart extends ApplicationAdapter {

    private Stage stage;
    private Skin skin;


    static public class Dia extends Window {

        public Dia(String title, Skin skin) {
            super(title, skin);
        }

        public Dia(String title, Skin skin, String styleName) {
            super(title, skin, styleName);
        }

        public Dia(String title, WindowStyle style) {
            super(title, style);
        }

    }

//    static public class B extends Button{
//
//    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void create() {

        stage = new Stage(new ScreenViewport());
        WireConnector.base = stage;

        //node = new Node(new Vector2(50, 50), "etet");
        //node.addDataInputPin(Pin.Data.FLOAT, "");
        //  node.addDataInputPin(Pin.Data.INT, "");
        // node.addDataInputPin(Pin.Data.STRING, "");
        //node.addDataInputPin(Pin.Data.BOOL, "");
        skin = new Skin(Gdx.files.internal("uiskin.json"));


        FunctionItem functionItem = new FunctionItem(skin);
        functionItem.setPosition(300, 200);
        //
        //  stage.addActor(functionItem);
//

        BeginNode begin = new BeginNode(new Vector2(300, 200));
        stage.addActor(begin);

        stage.addActor(new LogNode(new Vector2(350, 250)));
        stage.addActor(new LogNode(new Vector2(350, 300)));

        stage.addActor(new StringNode(new Vector2(400, 150)));
        stage.addActor(new IntegerNode(new Vector2(400, 250)));


        ControlTable controlTable = new ControlTable(skin);

        controlTable.setHeight(stage.getHeight());
        stage.addActor(controlTable);
//        VariableTable var = new VariableTable(skin);
//        var.setPosition(400, 200);
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//        var.addVariable("qwe");
//
//        stage.addActor(var);
//
//        Node node2 = new Node(new Vector2(50, 100), "sdfsdfsv");
//        node2.addDataOutputPin(Pin.Data.FLOAT, "ff");
//        node2.addDataOutputPin(Pin.Data.INT, "ff");
//        node2.addDataOutputPin(Pin.Data.STRING, "ff");
//        node2.addDataOutputPin(Pin.Data.BOOL, "ff");
//        node2.addDataInputPin(Pin.Data.FLOAT, "ss");
//        node2.addDataInputPin(Pin.Data.INT, "cvb");
//        node2.addDataInputPin(Pin.Data.STRING, "cvb");
//        node2.addDataInputPin(Pin.Data.BOOL, "bg");

//        IntegerNode n = new IntegerNode(new Vector2(100, 200));
        //  stage.addActor(n);
        Gdx.input.setInputProcessor(stage);

        TextButton b = new TextButton("qwe", skin);
        stage.setDebugAll(true);

        // stage.addActor(node);
        // stage.addActor(node2);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
