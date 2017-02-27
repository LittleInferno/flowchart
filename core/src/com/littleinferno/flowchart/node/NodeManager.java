package com.littleinferno.flowchart.node;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.UUID;

public class NodeManager {

    private Json json;

    public NodeManager() {
        json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setOutputType(JsonWriter.OutputType.json);
        json.setSerializer(SceneNodeManager.class, new SceneNodeManager.SceneNodeManagerSerializer());
    }

    public <T extends Node> void addSerializer(Class<T> type, Json.Serializer<T> serializer) {
        json.setSerializer(type, serializer);
    }

    public String save(SceneNodeManager sceneNodeManager) {
        return json.prettyPrint(sceneNodeManager);
    }

    public void save(SceneNodeManager sceneNodeManager, FileHandle fileHandle) {
        fileHandle.writeString(save(sceneNodeManager), false);
    }

    public SceneNodeManager load(String string) {
        return json.fromJson(SceneNodeManager.class, string);
    }

    public SceneNodeManager load(FileHandle fileHandle) {
        return load(fileHandle.readString());
    }

    static UUID getID() {
        return UUID.randomUUID();
    }
}
