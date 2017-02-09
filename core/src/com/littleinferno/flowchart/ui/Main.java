package com.littleinferno.flowchart.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.kotcrab.vis.ui.VisUI;
import com.littleinferno.flowchart.node.BeginNode;

public class Main extends Stage {

    private static TabbedPane tabs;
    private static Table uiTable;
    private Table container;
    public static Table activity = new Table();
    public static Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private static DragAndDrop dragAndDrop = new DragAndDrop();
    public static VisUI.SkinScale scale = VisUI.SkinScale.X1;

    //TODO remove it
    public static DragAndDrop dndFunctions = new DragAndDrop();

    public static TextArea console;

    public Main() {
        //    super(viewport);
        //super(new ScreenViewport());
        //  VisUI.load(scale);
        scale = VisUI.SkinScale.X1;

      //  skin =

                container = new Table();
        container.setFillParent(true);
        addActor(container);

        // control = new ControlTable();

        // if (scale == VisUI.SkinScale.X1)
        //     container.add(control).growY().width(310);
        // else
        //     container.add(control).growY().width(460);
        container.add(activity).expand().fill();

        tabs = new TabbedPane(skin);
        activity.addActor(tabs);

   //     activity.addActor(WireManager.instance);

        TabbedPane.Tab main = addWindow("main");

        BeginNode node = new BeginNode();
        node.setPosition(500, 200);
        addActor(node);

//        AddNode node1 = new AddNode();
//        node.setPosition(300, 200);
//        main.getContentTable().addActor(node1);
//        AddNode node2 = new AddNode();
//        node2.setPosition(300, 200);
//        main.getContentTable().addActor(node2);
//
//        IntegerNode node3 = new IntegerNode();
//        node3.setPosition(300, 200);
//        main.getContentTable().addActor(node3);
//
//        StringNode node4 = new StringNode();
//        node4.setPosition(300, 200);
//        main.getContentTable().addActor(node4);
//
//        BoolNode node5 = new BoolNode();
//        node5.setPosition(300, 200);
//        main.getContentTable().addActor(node5);

        console = new TextArea("", skin);
        console.setPosition(100, 100);
        activity.addActor(console);

        uiTable = new Table();

        Main.dndFunctions.addTarget(new DragAndDrop.Target(uiTable) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Actor node = (Actor) payload.getObject();

                Vector2 vec = uiTable.localToStageCoordinates(new Vector2(x, y));
                vec = uiTable.getStage().stageToScreenCoordinates(vec);

                screenToStageCoordinates(vec);


                node.setPosition(vec.x, vec.y);
                Main.this.addActor(node);
            }
        });

        //  setDebugAll(true);
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

    public static Table getUiTable() {
        return uiTable;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            ((OrthographicCamera) getCamera()).zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            ((OrthographicCamera) getCamera()).zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            getCamera().translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            getCamera().translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            getCamera().translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            getCamera().translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            getCamera().rotate(-3, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            getCamera().rotate(3, 0, 0, 1);
        }
        getCamera().update();
    }


    public class Gesture implements GestureDetector.GestureListener {

        private float scale1 = 1;

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            getCamera().position.x -= deltaX;
            getCamera().position.y += deltaY;

            getCamera().update();
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            float ratio = initialDistance / distance;
            scale1 *= ratio;
            if (scale1 > 1)
                ((OrthographicCamera) getCamera()).zoom = scale1;
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }
    }


}
