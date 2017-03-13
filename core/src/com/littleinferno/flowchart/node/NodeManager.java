package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.scene.Scene;
import com.littleinferno.flowchart.util.SerializeHelper;

import java.util.ArrayList;
import java.util.List;

public class NodeManager {

    private List<Node> nodes;

    private Scene scene;

    public NodeManager() {
        nodes = new ArrayList<>();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        registerInScene();
    }

    private void registerInScene() {
        Stream.of(nodes).forEach(node -> scene.addActor(node));
    }

    public PluginNode getBeginNode() {
        return Stream.of(nodes)
                .filter(scene -> scene.getName().equals("begin"))
                .map(PluginNode.class::cast)
                .findFirst()
                .orElseGet(() -> registerNode(Project.instance().getNodePluginManager().getStartNode()));
    }

    public <T extends Node> T createNode(Class<T> type, Object... args) {
        T node = SerializeHelper.createObject(type, args);
        registerNode(node);
        return node;
    }

    public <T extends Node> T registerNode(T node) {
        nodes.add(node);
        if (scene != null)
            scene.addActor(node);
        return node;
    }

    void deleteNode(Node node) {
        nodes.remove(node);
        scene.getRoot().removeActor(node);
    }

    public Node getNode(String id) {
        return Stream.of(nodes)
                .filter(value -> value.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find node with id:" + id));
    }

    public static class NodeManagerSerializer implements Json.Serializer<NodeManager> {
        @Override
        public void write(Json json, NodeManager object, Class knownType) {
            json.writeObjectStart();

            json.writeObjectStart("nodes");

            Stream.of(object.nodes)
                    .map(Node::getHandle)
                    .forEach(handle -> SerializeHelper.writeHandle(json, handle));

            json.writeObjectEnd();

            json.writeObjectEnd();
        }

        @Override
        public NodeManager read(Json json, JsonValue jsonData, Class type) {

            NodeManager nodeManager = new NodeManager();
            JsonValue data = jsonData.get("nodes");

            for (JsonValue valueMap = data.child; valueMap != null; valueMap = valueMap.next) {
                SerializeHelper.Pair read = SerializeHelper.readHandle(json, valueMap);
                nodeManager.createNode(read.type, read.classHandle);
            }

            return nodeManager;
        }
    }
}
