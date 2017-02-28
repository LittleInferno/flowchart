package com.littleinferno.flowchart.gui;


import com.littleinferno.flowchart.node.BeginNode;

public class MainScene extends Scene {

    public MainScene() {
        super("main", false);

        BeginNode node = getNodeManager().createNode(BeginNode.class);
        node.setPosition(500, 200);
        addActor(node);
    }
}
