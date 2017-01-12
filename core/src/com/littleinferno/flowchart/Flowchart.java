package com.littleinferno.flowchart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.littleinferno.flowchart.node.BeginNode;
import com.littleinferno.flowchart.ui.Main;
import com.littleinferno.flowchart.wire.WireConnector;

public class Flowchart extends ApplicationAdapter {

    private Main main;

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        main.getViewport().update(width, height, true);
    }

    @Override
    public void create() {

        main = new Main();
        WireConnector.base = main;

        //node = new Node(new Vector2(50, 50), "etet");
        //node.addDataInputPin(Pin.Data.FLOAT, "");
        //  node.addDataInputPin(Pin.Data.INT, "");
        // node.addDataInputPin(Pin.Data.STRING, "");
        //node.addDataInputPin(Pin.Data.BOOL, "");


        // FunctionItem functionItem = new FunctionItem(skin);
        //functionItem.setPosition(300, 200);
        //
        //  stage.addActor(functionItem);
//

        BeginNode begin = new BeginNode(new Vector2(300, 200));
        // stage.addActor(begin);

        ///  stage.addActor(new LogNode(new Vector2(350, 250)));
        //  stage.addActor(new LogNode(new Vector2(350, 300)));

        //  stage.addActor(new StringNode   (new Vector2(400, 150)));
        //  stage.addActor(new IntegerNode(new Vector2(400, 250)));


        Gdx.input.setInputProcessor(main);

   //     main.setDebugAll(true);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.act();
        main.draw();
    }

    @Override
    public void dispose() {
        main.dispose();
    }
}
