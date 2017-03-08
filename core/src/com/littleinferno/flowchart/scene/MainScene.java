package com.littleinferno.flowchart.scene;


import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.project.Project;

public class MainScene extends Scene {

    @SuppressWarnings("WeakerAccess")
    public MainScene(SceneHandle sceneHandle, Project project) {
        super(sceneHandle, project);
        getNodeManager().getBeginNode().setPosition(500, 200);
    }

    @SuppressWarnings("unused")
    public MainScene(NodeManager nodeManager, Project project) {
        this(new SceneHandle(nodeManager, "main", false), project);
    }
}
