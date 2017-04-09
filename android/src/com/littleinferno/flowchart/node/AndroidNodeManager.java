package com.littleinferno.flowchart.node;

import com.annimon.stream.Optional;
import com.littleinferno.flowchart.FlowchartProject;
import com.littleinferno.flowchart.Scene;

public class AndroidNodeManager {

    final private FlowchartProject flowchartProject;
    final private Scene scene;

    public AndroidNodeManager(Scene scene) {
        this.scene = scene;
        flowchartProject = scene.getProject();
    }

    public Optional<AndroidNode> createNode() {
        return Optional.of(new AndroidNode(scene, null));
    }
}
