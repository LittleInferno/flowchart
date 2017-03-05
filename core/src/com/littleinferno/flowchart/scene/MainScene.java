package com.littleinferno.flowchart.scene;


import com.littleinferno.flowchart.node.NodeManager;

public class MainScene extends Scene {

    @SuppressWarnings("WeakerAccess")
    public MainScene(SceneHandle sceneHandle) {
        super(sceneHandle);
        getNodeManager().getBeginNode().setPosition(500, 200);
    }

    @SuppressWarnings("unused")
    public MainScene(NodeManager nodeManager) {
        this(new SceneHandle(nodeManager, "main", false));
    }
}
