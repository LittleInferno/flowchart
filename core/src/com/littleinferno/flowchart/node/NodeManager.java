package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.littleinferno.flowchart.plugin.NodePluginManager;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.scene.Scene;

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

    public Node createNode(String type) {
        return createNode(scene.getProject().getNodePluginManager().getNodeHandle(type));
    }

    public Node createNode(String type, Node.NodeHandle nodeHandle) {
        return createNode(Project.instance().getNodePluginManager().getNodeHandle(type), nodeHandle);
    }

    private Node createNode(NodePluginManager.PluginNodeHandle handle) {
        return createNode(handle, new Node.NodeHandle(handle.title, handle.closable, handle.name));
    }

    private Node createNode(NodePluginManager.PluginNodeHandle pluginHandle, Node.NodeHandle nodeHandle) {
        return registerNode(getIfSingle(pluginHandle))
                .initFromHandle(nodeHandle);
    }

    private Node getIfSingle(NodePluginManager.PluginNodeHandle handle) {
        return handle.single ? getOrCreateNode(handle) : create(handle);
    }

    private Node getOrCreateNode(NodePluginManager.PluginNodeHandle handle) {
        return Stream.of(nodes)
                .filter(value -> value instanceof PluginNode)
                .map(PluginNode.class::cast)
                .filter(value -> value.getPluginHandle().name.equals(handle.name))
                .limit(1)
                .findFirst()
                .orElseGet(() -> create(handle));
    }

    private PluginNode create(NodePluginManager.PluginNodeHandle pluginHandle) {
        return new PluginNode(pluginHandle);
    }

    private <T extends Node> T registerNode(T node) {
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


            json.writeValue("nodes", Stream.of(object.nodes)
                    .filter(value -> value instanceof PluginNode)
                    .map(Node::getHandle)
                    .toArray());


            json.writeObjectEnd();
        }

        @Override
        public NodeManager read(Json json, JsonValue jsonData, Class type) {

            NodeManager nodeManager = new NodeManager();

            //noinspection unchecked
            List<Node.NodeHandle> list = json.readValue(List.class, Node.NodeHandle.class, jsonData.child());

            Stream.of(list).forEach(f -> nodeManager.createNode(f.name, f));

            return nodeManager;
        }
    }
}
