package com.littleinferno.flowchart.scene;

import com.annimon.stream.Stream;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.littleinferno.flowchart.node.NodeManager;
import com.littleinferno.flowchart.util.SerializeHelper;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private List<Scene> scenes;

    public SceneManager() {
        scenes = new ArrayList<>();
    }

    public MainScene getMainScene() {
        return (MainScene) Stream.of(scenes)
                .filter(scene -> scene.getName().equals("main"))
                .findFirst().orElseGet(this::createMain);
    }

    private MainScene createMain() {
        return createScene(MainScene.class, new NodeManager());
    }

    public <T extends Scene> T createScene(Class<T> type, Object... args) {
        T scene = SerializeHelper.createObject(type, args);
        scenes.add(scene);
        return scene;
    }

    public void deleteScene(Scene scene) {
        scenes.remove(scene);
    }

    public static class SceneManagerSerializer implements Json.Serializer<SceneManager> {
        @Override
        public void write(Json json, SceneManager object, Class knownType) {
            json.writeObjectStart();

            json.writeObjectStart("scenes");

            Stream.of(object.scenes)
                    .map(Scene::getHandle)
                    .forEach(handle -> SerializeHelper.writeHandle(json, handle));

            json.writeObjectEnd();

            json.writeObjectEnd();
        }

        @Override
        public SceneManager read(Json json, JsonValue jsonData, Class type) {

            SceneManager sceneManager = new SceneManager();
            JsonValue scenes = jsonData.get("scenes");

            for (JsonValue valueMap = scenes.child; valueMap != null; valueMap = valueMap.next) {
                SerializeHelper.Pair read = SerializeHelper.readHandle(json, valueMap);
                sceneManager.createScene(read.type, read.classHandle);
            }

            return sceneManager;
        }
    }
}
