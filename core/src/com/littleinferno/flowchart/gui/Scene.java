package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.littleinferno.flowchart.node.BeginNode;
import com.littleinferno.flowchart.wire.WireManager;

public class Scene extends Stage {

    private UiTab uiTab;
    private GestureDetector gesture;
    private WireManager wireManager;
    private SceneUi sceneUi;

    Scene(SceneUi sceneUi) {
        this.sceneUi = sceneUi;

        uiTab = new UiTab(this);
        gesture = new GestureDetector(new Gesture());
        wireManager = new WireManager();

        SceneUi.addDragAndDropTarget(new DragAndDrop.Target(uiTab.getContentTable()) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Actor node = (Actor) payload.getObject();

                Vector2 vec = uiTab.getContentTable().localToStageCoordinates(new Vector2(x, y));
                vec = uiTab.getContentTable().getStage().stageToScreenCoordinates(vec);

                screenToStageCoordinates(vec);

                node.setPosition(vec.x, vec.y);
                addActor(node);
            }
        });

        addActor(wireManager);

        BeginNode node = new BeginNode();
        node.setPosition(500, 200);
        addActor(node);
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

    class UiTab extends Tab {

        private VisTable uiTable;
        private Scene scene;

        private UiTab(Scene scene) {
            this.scene = scene;
            uiTable = new VisTable();
        }

        @Override
        public String getTabTitle() {
            return "tab";
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
}


