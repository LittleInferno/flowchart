package com.littleinferno.flowchart.gui;


import com.littleinferno.flowchart.node.NodeManager;

public class MainScene extends Scene {

    public MainScene(SceneHandle sceneHandle) {
        super(sceneHandle);
        getNodeManager().getBeginNode().setPosition(500, 200);
    }

    public MainScene(NodeManager nodeManager) {
        this(new SceneHandle(nodeManager, "main", false));
    }
}
