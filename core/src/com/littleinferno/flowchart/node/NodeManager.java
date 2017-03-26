package com.littleinferno.flowchart.node;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.littleinferno.flowchart.plugin.NodePluginHandle;
import com.littleinferno.flowchart.plugin.NodePluginManager;
import com.littleinferno.flowchart.project.Project;
import com.littleinferno.flowchart.scene.Scene;
import com.littleinferno.flowchart.util.SerializeHelper;

import java.util.ArrayList;
import java.util.List;

public class NodeManager {

    private List<Node> nodes;

    private Scene scene;

    public NodeManager(Scene scene, NodeManagerHandle nodeManagerHandle) {
        this.scene = scene;
        this.nodes = new ArrayList<>();

//        Stream.of(nodeManagerHandle.nodes)
//                .forEach(handle -> createNode(handle.getType(), handle));
    }

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

    public <T extends Node> T createNode(Class<T> type, Object... args) {
        T node = SerializeHelper.createObject(type, args);
        registerNode(node);
        return node;
    }


    public Optional<Node> createNode(String type) {
        NodePluginHandle.PluginNodeHandle handle = Stream.of(Project.pluginManager().getLoadedNodePlugins())
                .flatMap(nph -> Stream.of(nph.getNodes()))
                .filter(val -> val.getName().equals(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find node: " + type));

        return createNode(handle);
    }

    private Optional<Node> createNode(NodePluginHandle.PluginNodeHandle nodeHandle) {
        String s = nodeHandle
                .getAttribute("sceneType")
                .map(String.class::cast)
                .orElse("any");

        if (s.equals(scene.getType()) || s.equals("any"))
            return Optional.of(registerNode(getIfSingle(nodeHandle)));
        return Optional.empty();
    }

    private Node createNode2(NodePluginHandle.PluginNodeHandle pluginNodeHandle, Node.NodeParams nodeParams) {
        PluginNode pluginNode = new PluginNode(pluginNodeHandle);
        pluginNode.setParams(nodeParams);
        registerNode(pluginNode);
        return pluginNode;
    }

    private PluginNode create(NodePluginManager.PluginNodeHandle pluginHandle) {
        return new PluginNode(pluginHandle);
    }

    private Node getIfSingle(NodePluginHandle.PluginNodeHandle nodeHandle) {
        return nodeHandle.getAttribute("single").map(Boolean.class::cast).orElse(false)
                ? getOrCreateNode(nodeHandle)
                : create(nodeHandle);
    }

    private Node getOrCreateNode(NodePluginHandle.PluginNodeHandle nodeHandle) {
        return Stream.of(nodes)
                .map(PluginNode.class::cast) // TODO renmove it
                .filter(value -> value.getHandle().getName().equals(nodeHandle.getName()))
                .limit(1)
                .findFirst()
                .map(Node.class::cast) // TODO renmove it
                .orElseGet(() -> create(nodeHandle));
    }

    private PluginNode create(NodePluginHandle.PluginNodeHandle pluginNodeHandle) {
        return new PluginNode(pluginNodeHandle);
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

    public Optional<Node> getStartNode() {
        return Stream.of(nodes)
                .filter(value -> value instanceof PluginNode) // TODO renmove it
                .map(PluginNode.class::cast) // TODO renmove it
                .filter(value -> value.getPluginHandle().start)
                .limit(1)
                .map(Node.class::cast)        // TODO renmove it
                .findFirst();
    }

    public NodeManagerHandle getHandle() {
        return new NodeManagerHandle(Stream.of(nodes).map(Node::getParams).toList());
    }


    @SuppressWarnings("WeakerAccess")
    public static class NodeManagerHandle {
        public List<Node.NodeParams> nodes;

        public NodeManagerHandle() {
            nodes = new ArrayList<>();
        }

        public NodeManagerHandle(List<Node.NodeParams> nodes) {
            this.nodes = nodes;
        }
    }

    public static class NodeManagerSerializer implements Json.Serializer<NodeManager> {
        @Override
        public void write(Json json, NodeManager object, Class knownType) {
            json.writeObjectStart();


            json.writeValue("nodes", Stream.of(object.nodes)
                    .filter(value -> value instanceof PluginNode)
                    .map(Node::getParams)
                    .toArray());


            json.writeObjectEnd();
        }

        @Override
        public NodeManager read(Json json, JsonValue jsonData, Class type) {

            NodeManager nodeManager = new NodeManager();

            //noinspection unchecked
            List<Node.NodeParams> list = json.readValue(List.class, Node.NodeParams.class, jsonData.child());

        //    Stream.of(list).forEach(f -> nodeManager.createNode(f.getType(), f));

            return nodeManager;
        }
    }
}
