package com.littleinferno.flowchart.node;

import com.annimon.stream.Optional;
import com.littleinferno.flowchart.FlowchartProject;
import com.littleinferno.flowchart.Scene;
import com.littleinferno.flowchart.plugin.AndroidNodePluginHandle;

import java.util.ArrayList;
import java.util.List;

public class AndroidNodeManager {

    final private FlowchartProject project;
    final private Scene scene;
    final private List<AndroidNode> nodes;

    public AndroidNodeManager(Scene scene) {
        this.scene = scene;
        project = scene.getProject();
        nodes = new ArrayList<>();
    }

    public Optional<AndroidNode> createNode(String nodeName) {
        AndroidNodePluginHandle.NodeHandle nodeHandle = project
                .getPluginManager()
                .getNode(nodeName)
                .orElseThrow(() -> new RuntimeException("cannot find node: " + nodeName));

        String s = nodeHandle.getAttribute("sceneType").map(String.class::cast).orElse("any");

        if (s.equals("any") || s.equals(scene.getSceneType())) {
            return Optional.of(new AndroidNode(scene, nodeHandle));
        }

        return Optional.empty();
    }
}
