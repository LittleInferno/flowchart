package com.littleinferno.flowchart.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.littleinferno.flowchart.gui.DropItem;
import com.littleinferno.flowchart.node.Node;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.node.PluginNode;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.util.ClassHandle;
import com.littleinferno.flowchart.util.ProjectStage;
import com.littleinferno.flowchart.wire.WireManager;

public class Scene extends ProjectStage {

    private UiTab uiTab;
    private GestureDetector gesture;
    private WireManager wireManager;
    private NodeManager nodeManager;
    private String name;

    private SceneHandle sceneHandle;

    Scene(SceneHandle sceneHandle, Project project) {
        super(new ScreenViewport(), project);

        this.name = sceneHandle.name;

        uiTab = new UiTab(this, sceneHandle.closable);

        this.sceneHandle = sceneHandle;
        this.sceneHandle.className = this.getClass().getName();

        gesture = new GestureDetector(new Gesture());

        this.nodeManager = sceneHandle.nodeManager;
        this.nodeManager.setScene(this);

        wireManager = new WireManager(this);
        if (sceneHandle.wireManagerHandle != null)
            wireManager.init(sceneHandle.wireManagerHandle);
        addActor(wireManager);
        wireManager.toBack();

        project.getUiScene().addDragAndDropTarget(new DragAndDrop.Target(uiTab.getContentTable()) {
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

                if (object instanceof PluginNode) {
                    getNodeManager().registerNode((Node) object).setPosition(vec.x, vec.y);
                } else if (object instanceof Actor) {
                    Actor target = (Actor) object;

                    target.setPosition(vec.x, vec.y);

                    ((DropItem) target).init(Scene.this);
                } else {
                    Class type = (Class) object;
                    getNodeManager().createNode(type).setPosition(vec.x, vec.y);
                }
            }
        });

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

    public SceneHandle getHandle() {
        sceneHandle.name = name;
        sceneHandle.wireManagerHandle = wireManager.getHandle();
        return sceneHandle;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static class UiTab extends Tab {

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

    private class Gesture extends GestureDetector.GestureAdapter {

        private float scale1 = 1;

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (x < 310) return false;

            getCamera().position.x -= deltaX;
            getCamera().position.y += deltaY;

            getCamera().update();
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
    }

    @SuppressWarnings("WeakerAccess")
    public static class SceneHandle extends ClassHandle {
        public NodeManager nodeManager;
        public WireManager.WireManagerHandle wireManagerHandle;
        public String name;
        public boolean closable;

        public SceneHandle() {
        }

        public SceneHandle(NodeManager nodeManager, String name, boolean closeable) {
            this(null, nodeManager, null, name, closeable);
        }

        public SceneHandle(String className, NodeManager nodeManager, WireManager.WireManagerHandle wireManagerHandle, String name, boolean closable) {
            super(className);
            this.nodeManager = nodeManager;
            this.wireManagerHandle = wireManagerHandle;
            this.name = name;
            this.closable = closable;
        }
    }
}


