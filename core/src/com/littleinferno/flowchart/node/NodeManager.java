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

    public BeginNode getBeginNode() {
        return (BeginNode) Stream.of(nodes)
                .filter(scene -> scene.getName().equals("main"))
                .findFirst().orElseGet(() -> createNode(BeginNode.class, (Object[]) null));
    }

    public <T extends Node> T createNode(Class<T> type, Object... args) {

        if (args != null && type.equals(BeginNode.class)) {
            //noinspection unchecked
            return (T) getBeginNode();
        }

        T node;
        try {
            if (args != null && args.length > 0) {
                Class classes[] = new Class[args.length];
                for (int i = 0; i < classes.length; ++i)
                    classes[i] = args[i].getClass();

                node = type.getConstructor(classes).newInstance(args);
            } else
                node = type.getConstructor().newInstance();

            registerNode(node);
            return node;

        } catch (InvocationTargetException |
                IllegalAccessException |
                InstantiationException |
                NoSuchMethodException e) {
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
                    readObject(json, ClassReflection.forName(valueMap.name()), valueMap);
                } catch (ReflectionException ex) {
                    throw new SerializationException(ex);
                }
            }
            return nodeManager;
        }

        private void readObject(Json json, Class type, JsonValue valueMap) {

            Node.NodeHandle object = (Node.NodeHandle) json.readValue(type, valueMap);
            Node node = null;
            try {
                node = nodeManager.createNode(ClassReflection.forName(object.className), object);
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }

        private void writeNodes(Json json, List<Node> nodes) throws IOException {
            json.writeObjectStart("nodes");

            for (Node node : nodes) {
                Node.NodeHandle nodeHandle = node.getHandle();
                json.getWriter().name(nodeHandle.getClass().getName());
                json.writeValue(nodeHandle);
            }
            json.writeObjectEnd();
        }
    }
}
