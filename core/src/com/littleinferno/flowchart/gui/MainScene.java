package com.littleinferno.flowchart.gui;


import com.littleinferno.flowchart.node.NodeManager;

public class MainScene extends Scene {

    public MainScene(NodeManager nodeManager) {
        super("main", nodeManager, false);

        getNodeManager().getBeginNode().setPosition(500, 200);
    }
}
