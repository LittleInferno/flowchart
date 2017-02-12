package com.littleinferno.flowchart.gui;


import com.littleinferno.flowchart.node.BeginNode;

public class MainScene extends Scene {

    MainScene(SceneUi sceneUi) {
        super("main", false, sceneUi);

        BeginNode node = new BeginNode(sceneUi);
        node.setPosition(500, 200);
        addActor(node);
    }
}
