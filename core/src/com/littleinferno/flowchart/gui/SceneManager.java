package com.littleinferno.flowchart.gui;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SceneManager {

    private List<Scene> scenes;

    static UUID getID() {
        return UUID.randomUUID();
    }

    public SceneManager() {
        scenes = new ArrayList<>();
    }

    public <T extends Scene> T createScene(Class<T> type, Object... args) {
        T scene = null;
        try {
            scene = type.getConstructor().newInstance(args);
            scenes.add(scene);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return scene;
    }

    public void deleteScene(Scene scene) {
        scenes.remove(scene);
    }

    public static class SceneManagerSerializer implements Json.Serializer<SceneManager> {
        SceneManager sceneManager = new SceneManager();

        @Override
        public void write(Json json, SceneManager object, Class knownType) {
            json.writeObjectStart();

            try {
                writeScene(json, object.scenes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            json.writeObjectEnd();
        }

        @Override
        public SceneManager read(Json json, JsonValue jsonData, Class type) {

            JsonValue scenes = jsonData.get("scenes");

            for (JsonValue valueMap = scenes.child; valueMap != null; valueMap = valueMap.next) {
                try {
                    readObjects(json, ClassReflection.forName(valueMap.name()), valueMap);
                } catch (ReflectionException ex) {
                    throw new SerializationException(ex);
                }
            }

            return sceneManager;
        }

        private void readObjects(Json json, Class type, JsonValue valueMap) {

            Scene object = (Scene) json.readValue(type, valueMap);
            sceneManager.scenes.add(object);
        }

        private void writeScene(Json json, List<Scene> nodes) throws IOException {
            json.writeObjectStart("scenes");

            for (Scene node : nodes) {
                json.getWriter().name(node.getClass().getName());
                json.writeValue(node);
            }
            json.writeObjectEnd();
        }
    }


}
