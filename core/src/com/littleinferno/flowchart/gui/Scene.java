package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.wire.WireManager;

public class Scene extends Stage {

    private UiTab uiTab;
    private GestureDetector gesture;
    private WireManager wireManager;
    private NodeManager nodeManager;
    private String name;

    Scene(String name, boolean closeable) {
        super(new ScreenViewport());

        this.name = name;

        uiTab = new UiTab(this, closeable);
        gesture = new GestureDetector(new Gesture());

        wireManager = new WireManager();
        nodeManager = new NodeManager(this);

        Project.instance().getUiScene().addDragAndDropTarget(new DragAndDrop.Target(uiTab.getContentTable()) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                Object object = payload.getObject();

                Vector2 vec = uiTab.getContentTable().localToStageCoordinates(new Vector2(x, y));
                vec = uiTab.getContentTable().getStage().stageToScreenCoordinates(vec);

                screenToStageCoordinates(vec);

                if (object instanceof Actor) {
                    Actor target = (Actor) object;

                    target.setPosition(vec.x, vec.y);

                    ((DropItem) target).init(Scene.this);
                } else {
                    Class type = (Class) object;

                    Node node = Scene.this.getNodeManager().createNode(type);

                    node.setPosition(vec.x, vec.y);
                }
            }
        });

        addActor(wireManager);
    }

    public UiTab getUiTab() {
        return uiTab;
    }

    public GestureDetector getGesture() {
        return gesture;
    }

    public WireManager getWireManager() {
        return wireManager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NodeManager getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
        nodeManager.setScene(this);
    }

    static class UiTab extends Tab {

        private VisTable uiTable;
        private Scene scene;

        private UiTab(Scene scene, boolean closeable) {
            super(false, closeable);

            this.scene = scene;
            uiTable = new VisTable();
        }

        @Override
        public String getTabTitle() {
            return scene.getName();
        }

        @Override
        public Table getContentTable() {
            return uiTable;
        }

        public Scene getScene() {
            return scene;
        }


    }

    private class Gesture implements GestureDetector.GestureListener {

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
            if (x < 310) return false;

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

    public static class SceneSerializer<T extends Scene> implements Json.Serializer<T> {

        @Override
        public void write(Json json, T object, Class knownType) {

            json.writeObjectStart();
            json.writeValue("name", object.getName());
            json.writeValue("nodeManager", object.getNodeManager());
            json.writeObjectEnd();
        }

        @Override
        public T read(Json json, JsonValue jsonData, Class type) {

            T scene = null;
            try {
                //noinspection unchecked
                scene = (T) type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (scene != null) {
                scene.setNodeManager(json.readValue(NodeManager.class, jsonData.get("nodeManager")));
            }

            return scene;
        }
    }
}


