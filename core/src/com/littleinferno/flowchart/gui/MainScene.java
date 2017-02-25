package com.littleinferno.flowchart.gui;


import com.littleinferno.flowchart.node.BeginNode;

public class MainScene extends Scene {

    MainScene() {
        super("main", false);

        BeginNode node = new BeginNode();
        node.setPosition(500, 200);
        addActor(node);
    }
}
