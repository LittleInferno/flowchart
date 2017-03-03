package com.littleinferno.flowchart.node;

import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.littleinferno.flowchart.gui.Scene;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class NodeManager {

    private List<Node> nodes;

    private Scene scene;

    public NodeManager() {
        nodes = new ArrayList<>();
    }

    public NodeManager(Scene scene) {
        this.scene = scene;
        nodes = new ArrayList<>();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        registerInScene();
    }

    private void registerInScene() {
        Stream.of(nodes).forEach(node -> scene.addActor(node));
    }

    static public <T> T create(Class type, Object... args) {

        T node = null;
        try {
            //noinspection unchecked
            node = (T) type.getConstructors()[0].newInstance(args);

        } catch (IllegalAccessException |
                InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }

        return node;
    }

    public <T extends Node> T createNode(Class<T> nodeClass, Object... args) {

        T node;
        try {
            node = (T) nodeClass.getConstructors()[0].newInstance(args);
            registerNode(node);
            return node;

        } catch (IllegalAccessException |
                InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerNode(Node node) {
        nodes.add(node);
        if (scene != null)
            scene.addActor(node);
    }

    public void deleteNode(Node node) {
        nodes.remove(node);
        scene.getRoot().removeActor(node);
    }

    public static class NodeManagerSerializer implements Json.Serializer<NodeManager> {
        NodeManager nodeManager = new NodeManager();

        @Override
        public void write(Json json, NodeManager object, Class knownType) {

            json.writeObjectStart();

            try {
                writeNodes(json, object.nodes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            json.writeObjectEnd();
        }

        @Override
        public NodeManager read(Json json, JsonValue jsonData, Class type) {

            JsonValue data = jsonData.child;
            for (JsonValue valueMap = data.child; valueMap != null; valueMap = valueMap.next) {
                try {
                    readObjects(json, ClassReflection.forName(valueMap.name()), valueMap);
                } catch (ReflectionException ex) {
                    throw new SerializationException(ex);
                }
            }
            return nodeManager;
        }

        private void readObjects(Json json, Class type, JsonValue valueMap) {

            Node object = (Node) json.readValue(type, valueMap);
            nodeManager.registerNode(object);
        }

        private void writeNodes(Json json, List<Node> nodes) throws IOException {
            json.writeObjectStart("nodes");

            for (Node node : nodes) {
                json.getWriter().name(node.getClass().getName());
                json.writeValue(node);
            }
            json.writeObjectEnd();
        }
    }
}
