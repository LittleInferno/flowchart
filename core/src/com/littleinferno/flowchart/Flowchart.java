package com.littleinferno.flowchart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.littleinferno.flowchart.node.IntegerNode;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.pin.Pin;
import com.littleinferno.flowchart.ui.NodeTree;
import com.littleinferno.flowchart.ui.VariableItem;
import com.littleinferno.flowchart.ui.VariableTable;
import com.littleinferno.flowchart.wire.WireConnector;

public class Flowchart extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    private Node node;

    private com.littleinferno.flowchart.wire.Wire wire;

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
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        stage = new Stage();
        WireConnector.base = stage;

        node = new Node(new Vector2(50, 50), "etet");
        node.addDataInputPin(Pin.Data.FLOAT, "");
        //  node.addDataInputPin(Pin.Data.INT, "");
        // node.addDataInputPin(Pin.Data.STRING, "");
        node.addDataInputPin(Pin.Data.BOOL, "");
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        NodeTree tr = new NodeTree(skin);
        tr.setPosition(300, 300);
        stage.addActor(tr);


        VariableTable var = new VariableTable(skin);
        var.setPosition(50, 50);
        stage.addActor(var);

        Node node2 = new Node(new Vector2(50, 100), "sdfsdfsv");
        node2.addDataOutputPin(Pin.Data.FLOAT, "ff");
        node2.addDataOutputPin(Pin.Data.INT, "ff");
        node2.addDataOutputPin(Pin.Data.STRING, "ff");
        node2.addDataOutputPin(Pin.Data.BOOL, "ff");
        node2.addDataInputPin(Pin.Data.FLOAT, "ss");
        node2.addDataInputPin(Pin.Data.INT, "cvb");
        node2.addDataInputPin(Pin.Data.STRING, "cvb");
        node2.addDataInputPin(Pin.Data.BOOL, "bg");
        // node.addDataOutputPin(Pin.Data.BOOL, "");

        IntegerNode n = new IntegerNode(new Vector2(100, 200));
        stage.addActor(n);
        Gdx.input.setInputProcessor(stage);

        Button closeButton = new TextButton("X", skin, "default");
        Dia d = new Dia("ee", skin);

        TextButton b = new TextButton("qwe", skin);
        // stage.setDebugAll(true);

        stage.addActor(node);
        stage.addActor(node2);

        stage.addActor(d);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
